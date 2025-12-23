package com.example.bankcards.service.impl;

import com.example.bankcards.dto.request.UserCreateRequestDto;
import com.example.bankcards.dto.request.UserUpdateRequestDto;
import com.example.bankcards.dto.response.UserInfoResponseDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.ExceptionMessages;
import com.example.bankcards.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

/**
 * Реализация сервиса управления пользователями.
 * <p>
 * Предоставляет CRUD операции: создание, получение, обновление, удаление пользователей.
 * Также поддерживает пагинацию при получении списка пользователей.
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Создание нового пользователя с заданными ролями и хешированием пароля.
     *
     * @param request DTO с данными для создания пользователя
     * @return DTO с информацией о созданном пользователе
     */
    @Override
    public UserInfoResponseDto createUser(UserCreateRequestDto request) {
        String encodedPassword = passwordEncoder.encode(request.password());
        Set<Role> roles = roleRepository.findByNameIn(request.roles());

        User user = User.builder()
                .username(request.username())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .password(encodedPassword)
                .roles(roles)
                .build();
        log.info("Пользователь с ID = {} был создан", user.getId());
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    /**
     * Получение информации о пользователе по его UUID.
     *
     * @param userId UUID пользователя
     * @return DTO с информацией о пользователе
     */
    @Override
    public UserInfoResponseDto getUserById(UUID userId) {
        User user = getUser(userId);
        return userMapper.toDto(user);
    }

    /**
     * Получение всех пользователей с поддержкой пагинации.
     *
     * @param pageable объект пагинации (страница, размер, сортировка)
     * @return страница DTO пользователей
     */
    @Override
    public Page<UserInfoResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    /**
     * Частичное обновление данных пользователя (PATCH).
     * Обновляются только непустые поля.
     *
     * @param userId  UUID пользователя
     * @param request DTO с данными для обновления
     * @return DTO с обновленной информацией пользователя
     */
    @Override
    public UserInfoResponseDto updateUser(UUID userId, UserUpdateRequestDto request) {
        User user = getUser(userId);

        if (request.username() != null) user.setUsername(request.username());
        if (request.firstName() != null) user.setFirstName(request.firstName());
        if (request.lastName() != null) user.setLastName(request.lastName());
        if (request.roles() != null) {
            user.setRoles(roleRepository.findByNameIn(request.roles()));
        }

        User updated = userRepository.save(user);
        log.info("Пользователь с ID = {} был изменён", user.getId());
        return userMapper.toDto(updated);
    }

    /**
     * Удаление пользователя по его UUID.
     *
     * @param userId UUID пользователя
     */
    @Override
    public String deleteUser(UUID userId) {
        log.info("Пользователь с ID = {} был удален", userId);
        userRepository.deleteById(userId);
        return "Пользователь был успешно удалён";
    }

    /**
     * Достаёт карту и обрабатывает ситуацию когда карта не найдена
     *
     * @param userId идентификатор карты
     * @return карту
     */

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    String exMessage = ExceptionMessages.USER_NOT_FOUND_BY_ID.getDescription()
                            .formatted(userId);
                    log.warn(exMessage);
                    return new NotFoundException(exMessage);
                });
    }

}
