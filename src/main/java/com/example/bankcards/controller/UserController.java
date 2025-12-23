package com.example.bankcards.controller;

import com.example.bankcards.dto.request.UserCreateRequestDto;
import com.example.bankcards.dto.request.UserUpdateRequestDto;
import com.example.bankcards.dto.response.UserInfoResponseDto;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST-контроллер для управления пользователями.
 * <p>
 * Доступен только пользователям с ролью ADMIN.
 * Предоставляет CRUD операции и постраничный просмотр пользователей.
 * </p>
 */
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Создание пользователя
     *
     * @param request запрос на создание пользователя
     * @return данные о созданном пользователе
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserInfoResponseDto createUser(
            @RequestBody @Valid UserCreateRequestDto request
    ) {
        return userService.createUser(request);
    }

    /**
     * Получение пользователя по ID
     *
     * @param userId идентификатор пользователя
     * @return данные о пользователе
     */
    @GetMapping("/{userId}")
    public UserInfoResponseDto getUserById(
            @PathVariable UUID userId
    ) {
        return userService.getUserById(userId);
    }

    /**
     * Получение списка пользователей
     *
     * @param pageable данные для страниц
     * @return страницы с пользователями
     */
    @GetMapping
    public Page<UserInfoResponseDto> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    /**
     * Обновление пользователя
     *
     * @param userId  идентификатор пользователя
     * @param request запрос на обновление пользователя
     * @return обновлённый пользователь
     */
    @PatchMapping("/{userId}")
    public UserInfoResponseDto updateUser(
            @PathVariable UUID userId,
            @RequestBody @Valid UserUpdateRequestDto request
    ) {
        return userService.updateUser(userId, request);
    }

    /**
     * Удаление пользователя
     *
     * @param userId идентификатор пользователя
     * @return сообщение удалился ли пользователь
     */
    @DeleteMapping("/{userId}")
    public String deleteUser(
            @PathVariable UUID userId
    ) {
        return userService.deleteUser(userId);
    }
}
