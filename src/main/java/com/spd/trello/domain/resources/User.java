package com.spd.trello.domain.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spd.trello.domain.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class User extends Resource {
    @NotBlank(message = "Firstname shouldn`t be blank")
    private String firstName;
    @NotBlank(message = "Lastname shouldn`t be blank")
    private String lastName;
    @Pattern(regexp = "[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "Must be a valid email address")
    private String email;
    @JsonIgnore
    private String password;
}
