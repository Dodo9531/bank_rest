package com.example.bankcards.dto.request;

import com.example.bankcards.entity.enums.CardStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO для создания карты
 */

@Builder
public record CardCreateRequestDto(
        @NotNull
        String number,

        @NotNull
        CardStatus status,

        @NotNull
        LocalDate expiryDate,

        @NotNull
        BigDecimal balance
) {
}