package com.spd.trello;

import com.spd.trello.domain.User;
import com.spd.trello.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private final UserService userService;

    public Application( UserService userService) {
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(String... args) throws Exception {
        User user = userService.create("Petya", "Victor", "Doom", "email.com");
        System.out.println(user);
        System.out.println(userService.delete(user.getId()));

    }
}
