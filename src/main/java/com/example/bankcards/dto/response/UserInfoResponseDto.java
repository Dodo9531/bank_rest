package com.example.bankcards.dto.response;

import lombok.Builder;

import java.util.Set;

/**
 * DTO для получения данных об пользователе
 * <p>
 * Используется администратором для просмотра данных о пользователе
 * </p>
 * Содержит данные о пользователе и его ролях назначения ролей
 */

@Builder
public record UserInfoResponseDto(
        String username,

        String firstName,

        String lastName,

        Set<String> roles
) {
}
