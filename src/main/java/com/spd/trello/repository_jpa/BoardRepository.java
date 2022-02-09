package com.spd.trello.repository_jpa;

import com.spd.trello.domain.resources.Board;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoardRepository extends CommonRepository<Board> {
    List<Board> findAllByWorkSpaceId(UUID id);
}
