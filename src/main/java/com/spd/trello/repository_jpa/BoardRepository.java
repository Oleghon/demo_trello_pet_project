package com.spd.trello.repository_jpa;

import com.spd.trello.domain.resources.Board;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends CommonRepository<Board> {
}
