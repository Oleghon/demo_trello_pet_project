package com.spd.trello.domain.resources;

import com.spd.trello.domain.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.TimeZone;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class User extends Resource {
    private String firstName;
    private String lastName;
    private String email;
}
