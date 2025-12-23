package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое при некорректном запросе
 * <p>
 * Это подкласс RuntimeException и может использоваться для обработки ситуаций, когда получен некорректный запрос
 * </p>
 *
 * @author Smirnov Daniil
 */

public class BadRequestException extends RuntimeException {

    /**
     * Создает новый объект исключения с заданным сообщением.
     *
     * @param message сообщение об ошибке
     */

    public BadRequestException(String message) {
        super(message);
    }
}
