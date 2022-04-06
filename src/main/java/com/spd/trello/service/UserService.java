package com.spd.trello.service;

import com.spd.trello.domain.resources.User;
import com.spd.trello.repository_jpa.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends AbstractService<User, UserRepository> {

    public UserService(UserRepository repository) {
        super(repository);
    }

    public Optional<User> findUserByEmail(String email){
        return repository.findUserByEmail(email);
    }
}
