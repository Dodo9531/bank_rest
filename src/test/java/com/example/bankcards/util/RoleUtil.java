package com.example.bankcards.util;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.enums.RoleName;
import lombok.experimental.UtilityClass;

import java.util.Set;

/**
 * Класс для быстрого получения ролей для тестовых классов
 */

@UtilityClass
public class RoleUtil {

    public Role getRoleAdmin() {
        return new Role(1L, RoleName.ADMIN);
    }

    public Role getRoleUser() {
        return new Role(2L, RoleName.USER);
    }

    public Set<String> getRoleAdminSetWithString() {
        return Set.of("ADMIN");
    }

    public Set<String> getRoleUserSetWithString() {
        return Set.of("USER");
    }

}
