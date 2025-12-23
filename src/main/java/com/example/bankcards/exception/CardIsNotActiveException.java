package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое при попытке произвести операцию с картой которая не активна
 * <p>
 * Это подкласс RuntimeException и может использоваться для обработки ситуаций, когда пользователь
 * пытается произвести операцию с картой которая не является активной
 * </p>
 *
 * @author Smirnov Daniil
 */

public class CardIsNotActiveException extends RuntimeException {

    /**
     * Создает новый объект исключения с заданным сообщением.
     *
     * @param message сообщение об ошибке
     */

    public CardIsNotActiveException(String message) {
        super(message);
    }
}
