package com.example.bankcards.exception;


/**
 * Исключение, выбрасываемое когда токен не прошёл валидацию
 * <p>
 * Это подкласс RuntimeException и может использоваться для обработки ситуаций, когда токен не прошёл валидацию
 * </p>
 *
 * @author Smirnov Daniil
 */

public class InvalidTokenException extends RuntimeException {

    /**
     * Создает новый объект исключения с заданным сообщением.
     *
     * @param message сообщение об ошибке
     */

    public InvalidTokenException(String message) {
        super(message);
    }
}
