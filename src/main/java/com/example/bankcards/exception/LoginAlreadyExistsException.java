package com.example.bankcards.exception;


/**
 * Исключение, выбрасываемое при попытке зарегистрироваться с занятым логином.
 * <p>
 * Это подкласс RuntimeException и может использоваться для обработки ситуаций, когда новый пользователь пытается
 * зарегистрироваться с занятым логином
 * </p>
 *
 * @author Smirnov Daniil
 */

public class LoginAlreadyExistsException extends RuntimeException {
    /**
     * Создает новый объект исключения с заданным сообщением.
     *
     * @param message сообщение об ошибке
     */

    public LoginAlreadyExistsException(String message) {
        super(message);
    }
}
