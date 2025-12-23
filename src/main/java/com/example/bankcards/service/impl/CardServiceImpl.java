package com.example.bankcards.service.impl;

import com.example.bankcards.dto.request.CardCreateRequestDto;
import com.example.bankcards.dto.request.CardTransferRequestDto;
import com.example.bankcards.dto.response.CardInfoResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.CardIsNotActiveException;
import com.example.bankcards.exception.CardNotOwnedException;
import com.example.bankcards.exception.InsufficientBalanceException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.exception.SameCardTransferException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.CardService;
import com.example.bankcards.specification.CardSpecification;
import com.example.bankcards.util.mapper.CardMapper;
import com.example.bankcards.util.ExceptionMessages;
import com.example.bankcards.util.MaskCardNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сервис для управления картами пользователя.
 * <p>
 * Содержит бизнес-логику работы с картами:
 * создание, активацию, блокировку, удаление,
 * получение информации и переводы средств между картами.
 * </p>
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final TextEncryptor textEncryptor;

    /**
     * Создаёт новую банковскую карту.
     *
     * @param cardDto DTO с данными карты
     * @return DTO созданной карты
     */

    @Override
    public CardInfoResponseDto createCard(CardCreateRequestDto cardDto) {
        Card card = cardMapper.toCard(cardDto);
        card.setNumber(textEncryptor.encrypt(card.getNumber()));
        Card saved = cardRepository.save(card);
        log.info("Карта с ID = {} была создана", saved.getId());
        return cardMapper.toDto(saved);
    }

    /**
     * Метод для обновления статуса карты
     *
     * @param cardId UUID карты
     * @param status статус карты
     * @return сообщение с тем какой статус был установлен
     */

    @Override
    public String updateCardStatus(UUID cardId, CardStatus status) {
        Card card = getCard(cardId);
        card.setStatus(status);
        cardRepository.save(card);
        String message = String.format("Статус карты с ID = %s успешно изменен на %s", cardId, status.name());
        log.info(message);
        return message;
    }

    /**
     * Удаляет карту по её идентификатору.
     *
     * @param cardId идентификатор карты
     */

    @Override
    public String deleteCard(UUID cardId) {
        cardRepository.deleteById(cardId);
        log.info("Карта с ID={} была удалена", cardId);
        return "Карта была удалена";
    }

    /**
     * Возвращает список всех карт с пагинацией.
     *
     * @param pageable параметры пагинации
     * @return страница с DTO карт
     */

    @Override
    public Page<CardInfoResponseDto> getAllCards(UUID ownerId, String status, Pageable pageable) {
        Specification<Card> spec = Specification
                .where(CardSpecification.hasOwnerId(ownerId))
                .and(CardSpecification.hasStatus(status));

        return cardRepository.findAll(spec, pageable)
                .map(this::toDtoMasked);
    }

    /**
     * Возвращает список карт конкретного пользователя.
     *
     * @param userId   идентификатор пользователя
     * @param pageable параметры пагинации
     * @return страница с картами пользователя
     */

    @Override
    public Page<CardInfoResponseDto> getUserCards(UUID userId, Pageable pageable) {
        return cardRepository.findAllByOwnerId(userId, pageable)
                .map(this::toDtoMasked);
    }

    /**
     * Блокирует карту, принадлежащую пользователю.
     *
     * @param cardId идентификатор карты
     * @param userId идентификатор пользователя
     * @throws CardNotOwnedException если карта не принадлежит пользователю
     */

    @Override
    public String blockOwnedCard(UUID cardId, UUID userId) {
        Card card = getCardAndCheckOwnership(cardId, userId);
        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
        log.info("Пользователь с ID = {} заблокировал свою карту с ID = {}", userId, cardId);
        return "Карта была заблокирована по вашему запросу";
    }

    /**
     * Выполняет перевод средств между картами.
     * <p>
     * Проверяет принадлежность карты отправителя,
     * достаточность баланса и атомарно выполняет перевод.
     * </p>
     *
     * @param request DTO с параметрами перевода
     * @throws InsufficientBalanceException если недостаточно средств
     * @throws CardNotOwnedException        если карта не принадлежит пользователю
     */

    @Override
    public String transfer(CardTransferRequestDto request) {
        Card fromCard = getCardAndCheckOwnership(request.fromCardId(), request.userId());
        Card toCard = getCardAndCheckOwnership(request.toCardId(), request.userId());

        checkIfCardIsActive(fromCard);
        checkIfCardIsActive(toCard);

        checkCardBalanceForTransfer(fromCard, request.amount());
        checkFromCardAndToCard(request.fromCardId(), request.toCardId(), request.userId());

        fromCard.setBalance(fromCard.getBalance().subtract(request.amount()));
        toCard.setBalance(toCard.getBalance().add(request.amount()));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);
        log.info("Пользователь с ID = {} перевел {} рублей с карты с ID = {} на карту с ID = {}",
                request.userId(), request.amount(), fromCard.getId(), toCard.getId());
        return "Перевод прошёл успешно";
    }

    /**
     * Показывает баланс на карте
     *
     * @param cardId идентификатор карты
     * @param userId идентификатор пользователя
     * @return баланс на карте
     */

    @Override
    public BigDecimal getCardBalance(UUID cardId, UUID userId) {
        Card card = getCardAndCheckOwnership(cardId, userId);
        return card.getBalance();
    }

    /**
     * Проверяет принадлежит ли карта пользователю и выбрасывает исключение если не принадлежит
     *
     * @param cardId идентификатор карты
     * @param userId идентификатор пользователя
     * @return карту
     */

    private Card getCardAndCheckOwnership(UUID cardId, UUID userId) {
        Card card = getCard(cardId);
        if (!card.getOwnerId().equals(userId)) {
            String exMessage = ExceptionMessages.CARD_NOT_BELONGS_TO_YOU.getDescription().
                    formatted(userId, cardId);
            log.warn(exMessage);
            throw new CardNotOwnedException(exMessage);
        }
        return card;
    }

    /**
     * Достаёт карту и обрабатывает ситуацию когда карта не найдена
     *
     * @param cardId идентификатор карты
     * @return карту
     */

    private Card getCard(UUID cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> {
                    String exMessage = ExceptionMessages.CARD_NOT_FOUND.getDescription()
                            .formatted(cardId);
                    log.warn(exMessage);
                    return new NotFoundException(exMessage);
                });
    }

    /**
     * Проверяет хватает ли средств для перевода
     *
     * @param card        карта с которой хотят перевести деньги
     * @param requiredSum сумма, которую хотят перевести
     */
    private void checkCardBalanceForTransfer(Card card, BigDecimal requiredSum) {
        if (card.getBalance().compareTo(requiredSum) < 0) {
            String exMessage = ExceptionMessages.INSUFFICIENT_BALANCE.getDescription();
            log.warn(exMessage);
            throw new InsufficientBalanceException(exMessage);
        }
    }

    /**
     * Проверяет не пытаеются ли перевести деньги с карты на эту же карту
     *
     * @param fromCardId идентификатор карты с которой переводят
     * @param toCardId   идентификатор карты на которую переводят
     * @param userId     идентификатор пользователя совершающего перевод
     */

    private void checkFromCardAndToCard(UUID fromCardId, UUID toCardId, UUID userId) {
        if (fromCardId.equals(toCardId)) {
            String exMessage = ExceptionMessages.SAME_CARD_TRANSFER.getDescription()
                    .formatted(userId);
            log.warn(exMessage);
            throw new SameCardTransferException(exMessage);
        }
    }

    /**
     * Переводит данные из бд в ResponseDto с маскированным номером карты
     *
     * @param card Карта из бд
     * @return Данные карты с маскированными номером карты
     */
    private CardInfoResponseDto toDtoMasked(Card card) {
        String decryptedNumber = textEncryptor.decrypt(card.getNumber());
        String maskedNumber = MaskCardNumber.mask(decryptedNumber);
        return  CardInfoResponseDto.builder()
                .id(card.getId())
                .ownerId(card.getOwnerId())
                .number(maskedNumber)
                .status(card.getStatus())
                .expiryDate(card.getExpiryDate())
                .balance(card.getBalance())
                .build();
    }

    /**
     * Проверят является ли карта активной и если нет бросает исключение
     * @param card карта
     */

    private void checkIfCardIsActive(Card card) {
        if(!card.getStatus().equals(CardStatus.ACTIVE)) {
            throw new CardIsNotActiveException(ExceptionMessages.CARD_NOT_ACTIVE.getDescription());
        }
    }
}