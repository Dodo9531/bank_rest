package com.example.bankcards.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

/**
 * Константы для тестовых классов
 */

@UtilityClass
public class ConstantUtil {
    public final UUID VALID_CARD_ID_1 = UUID.fromString("f5f2f727-78b1-4b8e-ad35-de9b9661417e");
    public final UUID VALID_CARD_ID_2 = UUID.fromString("01319f65-a6c5-49b3-94f9-4cf2bfd9613a");
    public final UUID VALID_USER_ID_1 = UUID.fromString("01319f65-a6c5-49b3-94f9-4cf2bfd9613a");
    public final UUID VALID_USER_ID_2 = UUID.fromString("01319f65-a6c5-49b3-94f9-4cf2bfd9613a");
    public final String VALID_USER_FIRSTNAME = "Иван";
    public final String VALID_USER_LASTNAME = "Иванов";
    public final String VALID_USERNAME = "Dodo";
    public final String VALID_USERNAME_2 = "Dada";
    public final String VALID_PASSWORD = "PASSword123!";
    public final String ADMIN_USERS_CONTROLLER_URL = "/api/admin/users";
    public final String REGISTER_URL = "/auth/register";
    public final String LOGIN_URL = "/auth/login";
    public final String ROLE_USER = "USER";
    public final String ROLE_ADMIN = "ADMIN";
    public final String VALID_JWT_TOKEN = "jwt";
    public final String MASKED_CARD_NUMBER_1 = "**** **** **** 1234";
    public final String MASKED_CARD_NUMBER_2 = "**** **** **** 5678";
    public final String CARD_NUMBER_1 = "1234123412341234";
}
