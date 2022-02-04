package com.spd.trello.service;

import com.spd.trello.domain.resources.User;
import com.spd.trello.repository.impl.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService extends AbstractService<User> {

    private final UserRepositoryImpl userRepository;

    @Autowired
    public UserService(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }


    public User create(String createdBy, String firstName, String lastName, String email) {
        User user = new User();
        user.setCreatedBy(createdBy);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        return create(user);
    }

    @Override
    public User create(User obj) {
        userRepository.create(obj);
        return readById(obj.getId());
    }

    @Override
    public User update(UUID id, User obj) {
        obj.setUpdatedDate(LocalDateTime.now());
        userRepository.update(id, obj);
        return readById(id);
    }

    @Override
    public User readById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean delete(UUID id) {
        return userRepository.delete(id);
    }

    @Override
    public List<User> readAll() {
        return userRepository.getObjects();
    }

}
