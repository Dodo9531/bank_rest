package com.example.bankcards.util.mapper;

import com.example.bankcards.dto.response.UserInfoResponseDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper для преобразования User ↔ UserInfoResponseDto
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Преобразует объект User в UserInfoResponseDto
     *
     * @param user пользователь
     * @return Dto для ответа клиенту
     */
    UserInfoResponseDto toDto(User user);

    /**
     * Метод чтобы замапить роли из множества ролей в множество строк
     *
     * @param roles множество ролей
     * @return множество ролей в строках
     */
    default Set<String> mapRolesToStringSet(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());
    }
}
