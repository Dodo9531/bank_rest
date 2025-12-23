package com.example.bankcards.specification;

import com.example.bankcards.entity.Card;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

/**
 * Класс для фильтрации карт
 */

public class CardSpecification {

    public static Specification<Card> hasOwnerId(UUID ownerId) {
        return (root, query, cb) -> ownerId == null ? null : cb.equal(root.get("ownerId"), ownerId);
    }

    public static Specification<Card> hasStatus(String status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }
}
