package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое при попытке пользователя перевести деньги на туже карту с которой он и переводит
 * <p>
 * Это подкласс RuntimeException и может использоваться для обработки ситуаций, когда пользователь пытается
 * перевести деньги на туже карту с которой он и переводит
 * </p>
 *
 * @author Smirnov Daniil
 */

public class SameCardTransferException extends RuntimeException {

    /**
     * Создает новый объект исключения с заданным сообщением.
     *
     * @param message сообщение об ошибке
     */

    public SameCardTransferException(String message) {
        super(message);
    }
}
