package com.spd.trello.service;

import com.spd.trello.domain.resources.Label;
import com.spd.trello.repository.impl.LabelRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LabelService extends AbstractService<Label> {

    LabelRepositoryImpl labelRepository;

    @Autowired
    public LabelService(LabelRepositoryImpl labelRepository) {
        this.labelRepository = labelRepository;
    }

    @Override
    public Label create(Label obj) {
        labelRepository.create(obj);
        return readById(obj.getId());
    }

    @Override
    public Label update(UUID id, Label obj) {
        obj.setUpdatedDate(LocalDateTime.now());
        labelRepository.update(id, obj);
        return readById(id);
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
