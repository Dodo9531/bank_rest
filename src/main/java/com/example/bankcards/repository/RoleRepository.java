package com.example.bankcards.repository;

import com.example.bankcards.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Репозиторий для работы с таблицей ролей в базе данных
 */

public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Находит все роли, имена которых содержатся в переданном наборе.
     *
     * @param names набор имён ролей
     * @return набор ролей, соответствующих именам
     */
    Set<Role> findByNameIn(Set<String> names);
}
