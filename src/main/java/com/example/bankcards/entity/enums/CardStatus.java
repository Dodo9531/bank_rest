package com.example.bankcards.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum со всеми статусами карт
 */

@AllArgsConstructor
@Getter
public enum CardStatus {
    ACTIVE("Активная"),
    BLOCKED("Заблокированная"),
    EXPIRED("Истек срок действия");
    final String description;
}
