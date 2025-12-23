package com.example.bankcards.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Описание сообщений об ошибках для исключений.
 * <p>
 * Определяет текстовые сообщения для различных исключительных ситуаций.
 * </p>
 * @author Smirnov Daniil
 */

@Getter
@AllArgsConstructor
public enum ExceptionMessages {
    JWT_TOKEN_NOT_VALID("Jwt токен не валиден"),
    JWT_TOKEN_EXPIRED("Jwt токен истёк"),
    LOGIN_FAILED("Неверный логин и/или пароль"),
    LOGIN_IS_TAKEN("Пользователь с таким логином уже существует"),
    SAME_CARD_TRANSFER("Пользователь с ID = %s пытается перевести деньги на туже карту с которой переводит"),
    INSUFFICIENT_BALANCE("Средств на карте недостаточно"),
    CARD_NOT_BELONGS_TO_YOU("Пользователь с ID = %s пытался использовать карту с ID = %s которая ему не принадлежит"),
    USER_NOT_FOUND_BY_ID("Пользователь с ID = %s не найден"),
    USER_NOT_FOUND_BY_USERNAME("Пользователь с логином = %s не найден"),
    CARD_NOT_FOUND("Карта с ID = %s не найдена"),
    CARD_NOT_ACTIVE("Карта пользователя не активна");
    private final String description;
}
