package com.example.bankcards.util;

import lombok.experimental.UtilityClass;

/**
 * Класс для маскирования номера карты
 */
@UtilityClass
public class MaskCardNumber {
    public static String mask(String number) {
        if (number == null || number.length() < 4) return number;
        return "**** **** **** " + number.substring(number.length() - 4);
    }
}
