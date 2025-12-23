package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое при попытке потратить большее количество денег чем то что осталось на счету карты
 * <p>
 * Это подкласс RuntimeException и может использоваться для обработки ситуаций, когда пользователь
 * пытается потратить больше денег чем у него есть
 * </p>
 *
 * @author Smirnov Daniil
 */

public class InsufficientBalanceException extends RuntimeException {

    /**
     * Создает новый объект исключения с заданным сообщением.
     *
     * @param message сообщение об ошибке
     */

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
