package com.spd.trello.controller;

import com.spd.trello.domain.Board;
import com.spd.trello.service.BoardService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards")
public class BoardController extends AbstractController<Board, BoardService>{

    public BoardController(BoardService service) {
        super(service);
    }
}
