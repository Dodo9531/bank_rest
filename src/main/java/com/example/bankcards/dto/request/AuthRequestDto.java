package com.example.bankcards.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Запрос на логин
 */
public record AuthRequestDto(

        @NotBlank(message = "Логин не может быть пустым")
        @Size(min = 3, max = 30, message = "Логин должен быть от 3 до 30 символов")
        String username,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 8, max = 30, message = "Пароль должен быть от 8 до 30 символов")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
                message = "Пароль должен содержать хотя бы одну заглавную букву, одну строчную и одну цифру"
        )
        String password
) {
}
