package com.example.bankcards.util;

import com.example.bankcards.dto.request.CardCreateRequestDto;
import com.example.bankcards.dto.request.CardTransferRequestDto;
import com.example.bankcards.dto.response.CardInfoResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.CardStatus;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Класс для быстрого получения объектов класса карт для тестовых классов
 */

@UtilityClass
public class CardUtil {

    public Card getValidCard1() {
        return Card.builder()
                .id(ConstantUtil.VALID_CARD_ID_1)
                .balance(BigDecimal.valueOf(300))
                .ownerId(ConstantUtil.VALID_USER_ID_1)
                .status(CardStatus.ACTIVE).build();
    }

    public Card getValidCard2() {
        return Card.builder()
                .id(ConstantUtil.VALID_CARD_ID_2)
                .balance(BigDecimal.valueOf(1000))
                .ownerId(ConstantUtil.VALID_USER_ID_2)
                .status(CardStatus.ACTIVE).build();
    }

    public Card getValidCard1WithRandomUser() {
        return Card.builder()
                .id(ConstantUtil.VALID_CARD_ID_1)
                .balance(BigDecimal.valueOf(300))
                .ownerId(UUID.randomUUID())
                .status(CardStatus.ACTIVE).build();
    }

    public CardTransferRequestDto getTransferRequestDtoWithSameCardOnToAndFromFields() {
        return CardTransferRequestDto.builder()
                .fromCardId(ConstantUtil.VALID_CARD_ID_1)
                .toCardId(ConstantUtil.VALID_CARD_ID_1)
                .amount(BigDecimal.valueOf(200))
                .userId(ConstantUtil.VALID_USER_ID_1).build();
    }

    public CardTransferRequestDto getTransferRequestDtoWith99999Balance() {
        return CardTransferRequestDto.builder()
                .fromCardId(ConstantUtil.VALID_CARD_ID_1)
                .toCardId(ConstantUtil.VALID_CARD_ID_2)
                .amount(BigDecimal.valueOf(99999))
                .userId(ConstantUtil.VALID_USER_ID_1).build();
    }

    public CardTransferRequestDto getValidTransferRequestDto() {
        return CardTransferRequestDto.builder()
                .fromCardId(ConstantUtil.VALID_CARD_ID_1)
                .toCardId(ConstantUtil.VALID_CARD_ID_2)
                .amount(BigDecimal.valueOf(200))
                .userId(ConstantUtil.VALID_USER_ID_1).build();
    }

    public CardInfoResponseDto getValidCardInfoResponseDto() {
        return CardInfoResponseDto.builder()
                .id(ConstantUtil.VALID_CARD_ID_1)
                .ownerId(ConstantUtil.VALID_USER_ID_1)
                .number(ConstantUtil.MASKED_CARD_NUMBER_1)
                .status(CardStatus.ACTIVE)
                .expiryDate(null)
                .balance(BigDecimal.valueOf(300)).build();
    }

    public CardInfoResponseDto getSecondValidCardInfoResponseDto() {
        return CardInfoResponseDto.builder()
                .id(ConstantUtil.VALID_CARD_ID_2)
                .ownerId(ConstantUtil.VALID_USER_ID_2)
                .number(ConstantUtil.MASKED_CARD_NUMBER_2)
                .status(CardStatus.ACTIVE)
                .expiryDate(null)
                .balance(BigDecimal.valueOf(100)).build();
    }

    public CardCreateRequestDto getValidCardCreateRequestDto() {
        return CardCreateRequestDto.builder()
                .number(ConstantUtil.CARD_NUMBER_1)
                .status(CardStatus.ACTIVE)
                .expiryDate(LocalDate.now().plusYears(3))
                .balance(BigDecimal.valueOf(300))
                .build();
    }
}
