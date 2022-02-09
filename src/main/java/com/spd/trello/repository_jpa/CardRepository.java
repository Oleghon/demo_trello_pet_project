package com.spd.trello.repository_jpa;

import com.spd.trello.domain.resources.Card;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends CommonRepository<Card> {
}
