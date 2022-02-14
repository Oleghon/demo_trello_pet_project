package com.spd.trello.controller;

import com.spd.trello.domain.resources.Board;
import com.spd.trello.service.BoardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/boards")
public class BoardController extends AbstractController<Board, BoardService>{

    public BoardController(BoardService service) {
        super(service);
    }

    @GetMapping("space/{id}")
    public List<Board> getBoardsByWorkspace(@PathVariable UUID id){
        return service.findAllByWorkspace(id);
    }
}
