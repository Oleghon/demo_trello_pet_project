package com.spd.trello.repository_jpa;

import com.spd.trello.domain.resources.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends CommonRepository<Comment> {
    List<Comment> findAllByCardId(UUID id);
}
