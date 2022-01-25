package com.spd.trello.service;

import com.spd.trello.domain.Card;
import com.spd.trello.domain.Comment;
import com.spd.trello.repository.impl.CommentRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService extends AbstractService<Comment> {

    CommentRepositoryImpl commentRepository;

    @Autowired
    public CommentService(CommentRepositoryImpl commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment create(Card card, String createdBy, String text) {
        Comment comment = new Comment();
        comment.setCard(card);
        comment.setCreatedBy(createdBy);
        comment.setText(text);
        return create(comment);
    }

    @Override
    public Comment create(Comment obj) {
        commentRepository.create(obj);
        return readById(obj.getId());
    }

    @Override
    public Comment update(UUID id, Comment obj) {
        obj.setUpdatedDate(LocalDateTime.now());
        commentRepository.update(id, obj);
        return readById(id);
    }

    @Override
    public Comment readById(UUID id) {
        return commentRepository.findById(id);
    }

    @Override
    public boolean delete(UUID id) {
        return commentRepository.delete(id);
    }

    @Override
    public List<Comment> readAll() {
        return commentRepository.getObjects();
    }
}
