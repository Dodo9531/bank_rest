package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardCreateRequestDto;
import com.example.bankcards.dto.request.CardTransferRequestDto;
import com.example.bankcards.dto.response.CardInfoResponseDto;
import com.example.bankcards.entity.enums.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Сервис для управления банковскими картами
 * <p>
 * Предоставляет методы для создания, блокировки, активации, удаления карт,
 * получения информации о картах, перевода средств и просмотра баланса.
 * </p>
 */
public interface CardService {

    /**
     * Создание новой карты.
     *
     * @param card DTO с данными для создания карты
     * @return информация о созданной карте
     */
    CardInfoResponseDto createCard(CardCreateRequestDto card);

    /**
     * Изменение статуса карты
     *
     * @param cardId UUID карты
     */
    String updateCardStatus(UUID cardId, CardStatus status);

    /**
     * Удаление карты по её идентификатору.
     *
     * @param cardId UUID карты
     */
    String deleteCard(UUID cardId);

    /**
     * Получение всех карт с поддержкой пагинации.
     * Используется, например, администраторами для просмотра всех карт в системе.
     *
     * @param pageable объект пагинации (страница, размер, сортировка)
     * @return страница с информацией о картах
     */
    Page<CardInfoResponseDto> getAllCards(UUID ownerId, String status, Pageable pageable);

    /**
     * Получение карт конкретного пользователя с поддержкой пагинации.
     *
     * @param userId   UUID пользователя
     * @param pageable объект пагинации (страница, размер, сортировка)
     * @return страница с информацией о картах пользователя
     */
    Page<CardInfoResponseDto> getUserCards(UUID userId, Pageable pageable);

    /**
     * Запрос на блокировку карты, принадлежащей конкретному пользователю.
     * Используется, когда пользователь сам инициирует блокировку.
     *
     * @param cardId UUID карты
     * @param userId UUID пользователя
     */
    String blockOwnedCard(UUID cardId, UUID userId);

    /**
     * Перевод средств между картами пользователя.
     *
     * @param request DTO с информацией о переводе (с какой карты, на какую, сумма)
     */
    String transfer(CardTransferRequestDto request);

    /**
     * Получение текущего баланса карты пользователя.
     *
     * @param cardId UUID карты
     * @param userId UUID пользователя
     * @return текущий баланс карты
     */
    BigDecimal getCardBalance(UUID cardId, UUID userId);
}
