package com.spd.trello.controller;

import com.spd.trello.domain.resources.User;
import com.spd.trello.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User, UserService> {

    public UserController(UserService service) {
        super(service);
    }


}
