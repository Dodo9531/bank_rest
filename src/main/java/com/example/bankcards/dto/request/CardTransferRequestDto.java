package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record CardTransferRequestDto(
        @NotNull
        UUID fromCardId,

        @NotNull
        UUID toCardId,

        @NotNull
        @Positive(message = "Сумма перевода должна быть больше нуля")
        BigDecimal amount,

        @NotNull
        UUID userId
) {
}