package com.spd.trello.repository_jpa;

import com.spd.trello.domain.resources.Card;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardRepository extends CommonRepository<Card> {
    List<Card> findAllByCardListId(UUID id);
}
