package com.spd.trello.service;

import com.spd.trello.domain.Label;
import com.spd.trello.repository.impl.LabelRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class LabelService extends AbstractService<Label> {

    LabelRepositoryImpl labelRepository;

    public LabelService() {
        this.labelRepository = new LabelRepositoryImpl();
    }

    @Override
    public Label create(Label obj) {
        return labelRepository.create(obj);
    }

    @Override
    public Label update(UUID id, Label obj) {
        obj.setUpdatedDate(LocalDateTime.now());
        return labelRepository.update(id, obj);
    }

    @Override
    public Label readById(UUID id) {
        return labelRepository.findById(id);
    }

    @Override
    public boolean delete(UUID id) {
        return labelRepository.delete(id);
    }

    @Override
    public List<Label> readAll() {
        return null;
    }
}
