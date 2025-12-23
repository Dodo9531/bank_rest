package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

/**
 * DTO для обновления данных пользователя.
 * <p>
 * Используется администратором для частичного обновления информации о пользователе.
 * Поля являются опциональными.
 * </p>
 */

@Builder
public record UserUpdateRequestDto(

        @Size(min = 3, max = 100, message = "Логин должен быть от 3 до 100 символов")
        String username,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, max = 30, message = "Пароль должен быть от 8 до 30 символов")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
                message = "Пароль должен содержать хотя бы одну заглавную букву, одну строчную и одну цифру"
        )
        String password,

        @Size(max = 100, message = "Имя не должно превышать 100 символов")
        String firstName,

        @Size(max = 100, message = "Фамилия не должна превышать 100 символов")
        String lastName,

        Set<String> roles
) {
}
