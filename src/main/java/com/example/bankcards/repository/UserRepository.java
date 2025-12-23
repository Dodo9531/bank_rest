package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с пользователями в базе данных
 */

public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Возвращает пользователя по логину
     *
     * @param username логин пользователя
     * @return пользователь
     */
    Optional<User> findByUsername(String username);
}
