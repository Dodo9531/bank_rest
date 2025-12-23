package com.example.bankcards.exception;

/**
 * Исключение, выбрасываемое при отсутствии запрошенного ресурса.
 * <p>
 * Это подкласс RuntimeException и может использоваться для обработки ситуаций, когда не удается найти запрошенный
 * ресурс или объект.
 * </p>
 *
 * @author Smirnov Daniil
 */
public class NotFoundException extends RuntimeException {

    /**
     * Создает новый объект исключения с заданным сообщением.
     *
     * @param message сообщение об ошибке
     */

    public NotFoundException(String message) {
        super(message);
    }
}
