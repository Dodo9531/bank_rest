package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое при внутренней ошибке сервера.
 * <p>
 * Это подкласс RuntimeException и может использоваться для обработки ситуаций, когда происходят ошибки на сервере
 * </p>
 *
 * @author Smirnov Daniil
 */

public class InternalServerException extends RuntimeException {
    /**
     * Создает новый объект исключения с заданным сообщением.
     *
     * @param message сообщение об ошибке
     */
    public InternalServerException(String message) {
        super(message);
    }
}
