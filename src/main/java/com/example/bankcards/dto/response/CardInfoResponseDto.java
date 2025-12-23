package com.example.bankcards.dto.response;

import com.example.bankcards.entity.enums.CardStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO для ответа пользователю с данным карты
 */

@Builder
public record CardInfoResponseDto(
        UUID id,

        UUID ownerId,

        String number,

        CardStatus status,

        LocalDate expiryDate,

        BigDecimal balance
) {
}
