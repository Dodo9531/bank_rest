package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое при попытке произвести операцию с картой которая не принадлежит пользователю
 * <p>
 * Это подкласс RuntimeException и может использоваться для обработки ситуаций, когда пользователь
 * пытается произвести операцию с картой которая ему не принадлежит
 * </p>
 *
 * @author Smirnov Daniil
 */

public class CardNotOwnedException extends RuntimeException {

    /**
     * Создает новый объект исключения с заданным сообщением.
     *
     * @param message сообщение об ошибке
     */

    public CardNotOwnedException(String message) {
        super(message);
    }
}
