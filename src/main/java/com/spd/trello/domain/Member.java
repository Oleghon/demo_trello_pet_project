package com.spd.trello.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Member extends Resource {
    private User user;
    private Role role = Role.MEMBER;
}
