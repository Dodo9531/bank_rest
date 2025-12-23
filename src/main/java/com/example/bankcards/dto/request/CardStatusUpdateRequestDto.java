package com.example.bankcards.dto.request;


import com.example.bankcards.entity.enums.CardStatus;
import jakarta.validation.constraints.NotNull;

public record CardStatusUpdateRequestDto(
        @NotNull(message = "Статус не может быть пустым")
        CardStatus newStatus
) {
}
