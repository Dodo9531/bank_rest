package com.example.bankcards.util;

import com.example.bankcards.dto.request.AuthRequestDto;
import com.example.bankcards.dto.response.AuthResponseDto;
import lombok.experimental.UtilityClass;

/**
 * Класс для создания запросов аутентификации и ответов на эти запросы для тестовых классов
 */

@UtilityClass
public class AuthUtil {

    public AuthRequestDto getValidAuthRequestDto() {
        return new AuthRequestDto(
                ConstantUtil.VALID_USERNAME,
                ConstantUtil.VALID_PASSWORD
        );
    }

    public AuthResponseDto getValidAuthResponseDto() {
        return new AuthResponseDto(ConstantUtil.VALID_JWT_TOKEN);
    }
}
