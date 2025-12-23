package com.example.bankcards.exception.handler;

import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.exception.CardIsNotActiveException;
import com.example.bankcards.exception.CardNotOwnedException;
import com.example.bankcards.exception.ExpiredTokenException;
import com.example.bankcards.exception.InsufficientBalanceException;
import com.example.bankcards.exception.InvalidTokenException;
import com.example.bankcards.exception.LoginAlreadyExistsException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.util.ExceptionMessages;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Глобальный обработчик исключений для REST-контроллеров
 * <p>
 * Обрабатывает исключения, возникающие в контроллерах, и возвращает соответствующие ответы клиенту
 * </p>
 *
 * @author Smirnov Daniil
 */
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * Обрабатывает исключение InsufficientBalanceException и возвращает HTTP-ответ с кодом 400 BAD_REQUEST
     *
     * @param e исключение InsufficientBalanceException
     * @return ответ клиенту с сообщением об ошибке и кодом состояния 400
     */

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleInsufficientBalance(InsufficientBalanceException e) {
        return new ErrorResponseDto(e.getMessage());
    }

    /**
     * Обрабатывает исключение LoginAlreadyExistsException и возвращает HTTP-ответ с кодом 400 BAD_REQUEST
     *
     * @param ex исключение LoginAlreadyExistsException
     * @return ответ клиенту с сообщением об ошибке и кодом состояния 400
     */

    @ExceptionHandler(LoginAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleExpired(LoginAlreadyExistsException ex) {
        return new ErrorResponseDto(ex.getMessage());
    }

    /**
     * Обрабатывает исключение CardIsNotActiveException и возвращает HTTP-ответ с кодом 400 BAD_REQUEST
     *
     * @param ex исключение CardIsNotActiveException
     * @return ответ клиенту с сообщением об ошибке и кодом состояния 400
     */

    @ExceptionHandler(CardIsNotActiveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleCardNotActive(CardIsNotActiveException ex) {
        return new ErrorResponseDto(ex.getMessage());
    }

    /**
     * Обрабатывает исключение BadRequestException и возвращает HTTP-ответ с кодом 400 Bad Request.
     *
     * @param e исключение BadRequestException
     * @return ответ клиенту с сообщением об ошибке и кодом состояния 400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorResponseDto handleBadRequestException(RuntimeException e) {
        return new ErrorResponseDto(e.getMessage());
    }

    /**
     * Обработчик, перехватывающий ошибки валидации в ДТО и возвращает HTTP-ответ с кодом 400 BAD_REQUEST
     *
     * @param ex исключение вызванное ошибкой валидации
     * @return ответ клиенту с сообщением об ошибке и кодом состояния 400
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDto handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .findFirst()
                .map(ObjectError::getDefaultMessage)
                .orElse(ex.getMessage());

        return new ErrorResponseDto(errorMessage);
    }

    /**
     * Обрабатывает исключение InvalidTokenException и возвращает HTTP-ответ с кодом 401 UNAUTHORIZED
     *
     * @param ex исключение InvalidTokenException
     * @return ответ клиенту с сообщением об ошибке и кодом состояния 401
     */

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto handleExpired(InvalidTokenException ex) {
        return new ErrorResponseDto(ex.getMessage());
    }

    /**
     * Обрабатывает исключение ExpiredTokenException и возвращает HTTP-ответ с кодом 401 UNAUTHORIZED
     *
     * @param ex исключение ExpiredTokenException
     * @return ответ клиенту с сообщением об ошибке и кодом состояния 401
     */

    @ExceptionHandler(ExpiredTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto handleInvalid(ExpiredTokenException ex) {
        return new ErrorResponseDto(ex.getMessage());
    }

    /**
     * Обрабатывает исключение BadCredentialsException и возвращает HTTP-ответ с кодом 401 UNAUTHORIZED
     *
     * @return ответ клиенту с сообщением об ошибке и кодом состояния 401
     */

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto handleBadCredentials() {
        return new ErrorResponseDto(ExceptionMessages.LOGIN_FAILED.getDescription());
    }

    /**
     * Обрабатывает исключение CardNotOwnedException и возвращает HTTP-ответ с кодом 403 FORBIDDEN
     *
     * @param e исключение CardNotOwnedException
     * @return ответ клиенту с сообщением об ошибке и кодом состояния 403
     */

    @ExceptionHandler(CardNotOwnedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDto handleCardNotOwned(CardNotOwnedException e) {
        return new ErrorResponseDto(e.getMessage());
    }

    /**
     * Обрабатывает исключение NotFoundException и возвращает HTTP-ответ с кодом 404 Not Found
     *
     * @param e исключение NotFoundException
     * @return ответ клиенту с сообщением об ошибке и кодом состояния 404
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponseDto handleNotFoundException(RuntimeException e) {
        return new ErrorResponseDto(e.getMessage());
    }

    /**
     * Обрабатывает исключение UsernameNotFoundException и возвращает HTTP-ответ с кодом 404 Not Found
     *
     * @param e исключение UsernameNotFoundException
     * @return ответ клиенту с сообщением об ошибке и кодом состояния 404
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ErrorResponseDto handleUsernameNotFoundException(RuntimeException e) {
        return new ErrorResponseDto(e.getMessage());
    }

}