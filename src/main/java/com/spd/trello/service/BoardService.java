package com.spd.trello.service;

import com.spd.trello.domain.resources.Board;
import com.spd.trello.repository_jpa.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BoardService extends AbstractService<Board, BoardRepository> {
    public BoardService(BoardRepository repository) {
        super(repository);
    }

    public List<Board> findAllByWorkspace(UUID id){
        return repository.findAllByWorkSpaceId(id);
    }
}
