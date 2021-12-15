package com.spd.trello.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.TimeZone;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends Resource{
    private String firstName;
    private String lastName;
    private String email;
    private TimeZone timeZone;
}
