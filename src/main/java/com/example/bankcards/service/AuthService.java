package com.example.bankcards.service;

import com.example.bankcards.dto.request.AuthRequestDto;
import com.example.bankcards.dto.request.RegisterRequestDto;
import com.example.bankcards.dto.response.AuthResponseDto;

/**
 * Сервис для управления авторизацией
 * <p>
 * Предоставляет методы для регистрации нового пользователя и возможность входа в аккаунт
 * </p>
 */

public interface AuthService {

    /**
     * Метод для авторизации
     *
     * @param request запрос на авторизацию с логином и паролем
     * @return jwt токен
     */
    AuthResponseDto login(AuthRequestDto request);

    /**
     * Метод для регистрации
     *
     * @param request запрос на регистрацию с именем, фамилией, логином и паролем
     * @return Строка с ответом получилось ли авторизоваться
     */
    String register(RegisterRequestDto request);
}
