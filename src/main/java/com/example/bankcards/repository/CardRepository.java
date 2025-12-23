package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для работы с таблицей карт в базе данных
 */

@Repository
public interface CardRepository extends JpaRepository<Card, UUID>, JpaSpecificationExecutor<Card> {

    /**
     * Возвращает все карты пользователя
     *
     * @param userId   Идентификатор пользователя
     * @param pageable параметры пагинации и сортировки
     * @return страница карт всех пользователя
     */
    Page<Card> findAllByOwnerId(UUID userId, Pageable pageable);
}
