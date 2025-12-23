package com.example.bankcards.exception.handler;

/**
 * DTO для представления сообщения об ошибке.
 * <p>
 * Используется для передачи сообщений об ошибках от сервисов или контроллеров.
 * </p>
 *
 * @author Smirnov Daniil
 */
public record ErrorResponseDto(String errorMessage) {
}