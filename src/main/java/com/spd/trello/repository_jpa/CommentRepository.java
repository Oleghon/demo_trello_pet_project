package com.spd.trello.repository_jpa;

import com.spd.trello.domain.resources.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CommonRepository<Comment> {
}
