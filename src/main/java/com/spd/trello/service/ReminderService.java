package com.spd.trello.service;

import com.spd.trello.domain.Reminder;
import com.spd.trello.repository.impl.ReminderRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ReminderService extends AbstractService<Reminder> {

    ReminderRepositoryImpl reminderRepository;

    public ReminderService() {
        this.reminderRepository = new ReminderRepositoryImpl();
    }

    @Override
    public Reminder create(Reminder obj) {
        reminderRepository.create(obj);
        return readById(obj.getId());
    }

    @Override
    public Reminder update(UUID id, Reminder obj) {
        obj.setUpdatedDate(LocalDateTime.now());
        reminderRepository.update(id, obj);
        return readById(id);
    }

    @Override
    public Reminder readById(UUID id) {
        return reminderRepository.findById(id);
    }

    @Override
    public boolean delete(UUID id) {
        return reminderRepository.delete(id);
    }

    @Override
    public List<Reminder> readAll() {
        return reminderRepository.getObjects();
    }
}
