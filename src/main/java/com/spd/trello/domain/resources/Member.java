package com.spd.trello.domain.resources;

import com.spd.trello.domain.Resource;
import com.spd.trello.domain.enums.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "members")
public class Member extends Resource {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Role role = Role.MEMBER;
}
