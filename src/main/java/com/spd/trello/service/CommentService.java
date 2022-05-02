package com.spd.trello.service;

import com.spd.trello.domain.resources.Comment;
import com.spd.trello.repository_jpa.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService extends AbstractService<Comment, CommentRepository> {

    public CommentService(CommentRepository repository) {
        super(repository);
    }
}
