package com.spd.trello.repository_jpa;

import com.spd.trello.domain.resources.Card;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends CommonRepository<Card> {
    Card findCardByReminderId(UUID id);
}
