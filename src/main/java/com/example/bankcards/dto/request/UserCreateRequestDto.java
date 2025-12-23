package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

/**
 * DTO для создания нового пользователя
 * <p>
 * Используется администратором для добавления нового пользователя в систему.
 * </p>
 * Содержит данные необходимые для регистрации пользователя и назначения ролей.
 */

@Builder
public record UserCreateRequestDto(

        @NotBlank(message = "Логин не может быть пустым")
        @Size(min = 3, max = 100, message = "Логин должен быть от 3 до 100 символов")
        String username,

        @NotBlank(message = "Имя не может быть пустым")
        @Size(max = 100, message = "Имя не должно превышать 100 символов")
        String firstName,

        @NotBlank(message = "Фамилия не может быть пустой")
        @Size(max = 100, message = "Фамилия не должна превышать 100 символов")
        String lastName,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, max = 100, message = "Пароль должен быть от 8 до 100 символов")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
                message = "Пароль должен содержать хотя бы одну заглавную букву, одну строчную и одну цифру"
        )
        String password,

        @NotEmpty(message = "Список ролей не может быть пустым")
        Set<String> roles
) {
}
