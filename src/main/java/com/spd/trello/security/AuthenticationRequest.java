package com.spd.trello.security;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}
