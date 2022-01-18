package com.spd.trello.service;

import com.spd.trello.domain.Board;
import com.spd.trello.repository.Repository;
import com.spd.trello.repository.impl.BoardRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BoardService extends AbstractService<Board> {

    private Repository<Board> repository;

    public BoardService() {
        repository = new BoardRepositoryImpl();
    }

    @Override
    public Board create(Board obj) {
        repository.create(obj);
        return readById(obj.getId());
    }

    @Override
    public Board update(UUID id, Board obj) {
        obj.setUpdatedDate(LocalDateTime.now());
        repository.update(id, obj);
        return readById(id);
    }

    @Override
    public Board readById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public boolean delete(UUID id) {
        return repository.delete(id);
    }

    @Override
    public List<Board> readAll() {
        return repository.getObjects();
    }
}
