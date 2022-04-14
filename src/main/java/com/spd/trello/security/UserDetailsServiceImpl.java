package com.spd.trello.security;

import com.spd.trello.domain.resources.User;
import com.spd.trello.exception.EntityNotFoundException;
import com.spd.trello.repository_jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("detailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository repository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByEmail(username).orElseThrow(() -> new EntityNotFoundException("User doesn`t exist"));
    return SecureUser.fromUser(user);
    }
}
