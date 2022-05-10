package com.spd.trello.controller;

import com.spd.trello.domain.resources.Board;
import com.spd.trello.domain.resources.Member;
import com.spd.trello.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@Slf4j
@RestController
@RequestMapping("/boards")
public class BoardController extends AbstractController<Board, BoardService>{

    public BoardController(BoardService service) {
        super(service);
    }

    @PostMapping("/{id}/add_member")
    public ResponseEntity<Board> addMember(@PathVariable UUID id, @RequestBody Member member){
        Board board = service.addMember(id, member);
        log.info("entity with id: {} has just been added to board`: {}", member.getId(), board.getId());
        return new ResponseEntity<>(board, HttpStatus.OK);
    }
}
