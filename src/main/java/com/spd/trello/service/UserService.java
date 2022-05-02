package com.spd.trello.service;

import com.spd.trello.domain.resources.User;
import com.spd.trello.repository_jpa.UserRepository;
import com.spd.trello.security.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends AbstractService<User, UserRepository> {

    private MemberService memberService;

    @Autowired
    public UserService(UserRepository repository, MemberService memberService) {
        super(repository);
        this.memberService = memberService;
    }

    public Optional<User> findUserByEmail(String email) {
        return repository.findUserByEmail(email);
    }

    public User save(RegistrationRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());
        user = super.create(user);
        memberService.createDefaultMember(user.getId());
        return user;
    }
}
