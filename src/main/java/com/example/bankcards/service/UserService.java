package com.example.bankcards.service;

import com.example.bankcards.dto.request.UserCreateRequestDto;
import com.example.bankcards.dto.request.UserUpdateRequestDto;
import com.example.bankcards.dto.response.UserInfoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Сервис для управления пользователями.
 * <p>
 * Предоставляет методы для создания, получения, обновления и удаления пользователей,
 * а также для получения списка пользователей с поддержкой пагинации.
 * </p>
 */
public interface UserService {

    /**
     * Создание нового пользователя.
     *
     * @param request DTO с данными для создания пользователя
     * @return информация о созданном пользователе
     */
    UserInfoResponseDto createUser(UserCreateRequestDto request);

    /**
     * Получение информации о пользователе по его идентификатору.
     *
     * @param id UUID пользователя
     * @return информация о пользователе
     */
    UserInfoResponseDto getUserById(UUID id);

    /**
     * Получение всех пользователей с поддержкой пагинации.
     * Используется, например, администраторами для просмотра всех пользователей системы.
     *
     * @param pageable объект пагинации (страница, размер, сортировка)
     * @return страница с информацией о пользователях
     */
    Page<UserInfoResponseDto> getAllUsers(Pageable pageable);

    /**
     * Обновление данных существующего пользователя.
     *
     * @param id      UUID пользователя
     * @param request DTO с полями для обновления
     * @return обновленная информация о пользователе
     */
    UserInfoResponseDto updateUser(UUID id, UserUpdateRequestDto request);

    /**
     * Удаление пользователя по его идентификатору.
     *
     * @param id UUID пользователя
     * @return Сообщение был ли удален пользователь
     */
    String deleteUser(UUID id);
}
