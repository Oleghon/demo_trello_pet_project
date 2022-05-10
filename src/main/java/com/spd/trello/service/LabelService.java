package com.spd.trello.service;

import com.spd.trello.domain.resources.Label;
import com.spd.trello.repository_jpa.LabelRepository;
import org.springframework.stereotype.Service;

@Service
public class LabelService extends AbstractService<Label, LabelRepository> {

    public LabelService(LabelRepository repository) {
        super(repository);
    }
}
