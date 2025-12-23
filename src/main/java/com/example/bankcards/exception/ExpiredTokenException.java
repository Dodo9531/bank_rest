package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое если токен истёк
 * <p>
 * Это подкласс RuntimeException и может использоваться для обработки ситуаций, когда токен пользователя истёк
 * </p>
 *
 * @author Smirnov Daniil
 */

public class ExpiredTokenException extends RuntimeException {

    /**
     * Создает новый объект исключения с заданным сообщением.
     *
     * @param message сообщение об ошибке
     */

    public ExpiredTokenException(String message) {
        super(message);
    }
}
