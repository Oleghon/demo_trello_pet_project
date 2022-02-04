package com.spd.trello.controller;

import com.spd.trello.domain.resources.Comment;
import com.spd.trello.service.CommentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentController extends AbstractController<Comment, CommentService>{

    public CommentController(CommentService service) {
        super(service);
    }
}
