package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое при попытке доступа к ресурсам без авторизации
 * <p>
 * Это подкласс RuntimeException и может использоваться для обработки ситуаций, когда не авторизированный пользователь
 * пытается получить доступ к ресурсам
 * </p>
 *
 * @author Smirnov Daniil
 */

public class UnauthorizedException extends RuntimeException {

    /**
     * Создает новый объект исключения с заданным сообщением.
     *
     * @param message сообщение об ошибке
     */

    public UnauthorizedException(String message) {
        super(message);
    }
}
