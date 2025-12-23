package com.example.bankcards.util;

import com.example.bankcards.dto.request.RegisterRequestDto;
import lombok.experimental.UtilityClass;

/**
 * Класс для быстрого создания запросов на регистрацию
 */

@UtilityClass
public class RegisterUtil {

    public RegisterRequestDto getValidRegisterRequestDto() {
        return RegisterRequestDto.builder()
                .username(ConstantUtil.VALID_USERNAME)
                .password(ConstantUtil.VALID_PASSWORD)
                .firstName(ConstantUtil.VALID_USER_FIRSTNAME)
                .lastName(ConstantUtil.VALID_USER_LASTNAME).build();
    }
}
