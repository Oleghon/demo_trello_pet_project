package com.spd.trello.domain.resources;

import com.spd.trello.domain.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.TimeZone;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class User extends Resource {
    @NotBlank(message = "Firstname shouldn`t be blank")
    private String firstName;
    @NotBlank(message = "Lastname shouldn`t be blank")
    private String lastName;
    @Email(message = "Not correct email")
    private String email;
}
