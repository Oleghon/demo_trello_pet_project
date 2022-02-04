package com.spd.trello.domain.resources;

import com.spd.trello.domain.Resource;
import com.spd.trello.domain.enums.Role;
import com.spd.trello.domain.resources.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Member extends Resource {
    @EqualsAndHashCode.Exclude
    private User user;
    private Role role = Role.MEMBER;
}
