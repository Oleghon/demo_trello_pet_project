package com.spd.trello.repository_jpa;

import com.spd.trello.domain.resources.CardList;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardListRepository extends CommonRepository<CardList> {
    List<CardList> findAllByBoardId(UUID id);
}
