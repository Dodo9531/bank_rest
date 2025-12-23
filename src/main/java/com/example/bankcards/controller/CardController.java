package com.example.bankcards.controller;

import com.example.bankcards.dto.request.CardCreateRequestDto;
import com.example.bankcards.dto.request.CardTransferRequestDto;
import com.example.bankcards.dto.response.CardInfoResponseDto;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.UUID;

/**
 * REST-контроллер для управления банковскими картами
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;

    /* ======================================================
                            ADMIN
       ====================================================== */

    /**
     * Создание карты
     *
     * @param request запрос на создание карты
     * @return информация о созданной карте
     */
    @PostMapping("/admin/cards")
    @ResponseStatus(HttpStatus.CREATED)
    public CardInfoResponseDto createCard(
            @RequestBody @Valid CardCreateRequestDto request
    ) {
        return cardService.createCard(request);
    }

    /**
     * Получение всех карт
     *
     * @param pageable данные для страниц
     * @return страницы с всеми картами
     */
    @GetMapping("/admin/cards")
    public Page<CardInfoResponseDto> getAllCards(Pageable pageable,
                                                 @RequestParam(required = false) UUID ownerId,
                                                 @RequestParam(required = false) String status) {

        return cardService.getAllCards(ownerId, status, pageable);
    }

    /**
     * Изменение статуса карты
     *
     * @param cardId идентификатор карты
     * @param status новый статус
     * @return сообщение получилось ли изменить карту
     */
    @PatchMapping("/admin/cards/{cardId}/status")
    public String changeCardStatus(
            @PathVariable UUID cardId,
            @RequestParam CardStatus status
    ) {
        return cardService.updateCardStatus(cardId, status);
    }

    /**
     * Удаление карты
     *
     * @param cardId идентификатор карты
     * @return сообщение получилось ли удалить карту
     */
    @DeleteMapping("/admin/cards/{cardId}")
    public String deleteCard(
            @PathVariable UUID cardId
    ) {
        return cardService.deleteCard(cardId);
    }

    /* ======================================================
                            USER
       ====================================================== */

    /**
     * Получение пользователем списка своих карт
     *
     * @param principal объект с данными пользователя из Spring Security
     * @param pageable  данные для страниц
     * @return страницы карт
     */
    @GetMapping("/user/cards")
    public Page<CardInfoResponseDto> getUserCards(
            Principal principal,
            Pageable pageable
    ) {
        return cardService.getUserCards(UUID.fromString(principal.getName()), pageable);
    }

    /**
     * Блокировка своей карты
     *
     * @param principal объект с данными пользователя из Spring Security
     * @param cardId    идентификатор карты
     * @return сообщение была ли заблокирована карта
     */
    @PatchMapping("/user/cards/{cardId}/block")
    public String blockOwnCard(
            Principal principal,
            @PathVariable UUID cardId
    ) {
        return cardService.blockOwnedCard(cardId, UUID.fromString(principal.getName()));
    }

    /**
     * Перевод между своими картами
     *
     * @param request запрос на перевод
     * @return сообщение прошел ли перевод
     */
    @PostMapping("/user/cards/transfer")
    public String transfer(
            @RequestBody @Valid CardTransferRequestDto request
    ) {
        return cardService.transfer(request);
    }

    /**
     * Просмотр баланса своей карты
     *
     * @param principal
     * @param cardId
     * @return
     */
    @GetMapping("/user/cards/{cardId}/balance")
    public BigDecimal getBalance(
            Principal principal,
            @PathVariable UUID cardId
    ) {
        return cardService.getCardBalance(cardId, UUID.fromString(principal.getName()));
    }
}
