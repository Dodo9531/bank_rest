package com.example.bankcards.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum с названием ролей пользователя
 */

@AllArgsConstructor
@Getter
public enum RoleName {
    USER("USER"),
    ADMIN("ADMIN");
    final String name;
}
