package com.spd.domain;

import java.util.TimeZone;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private TimeZone timeZone;

    public User(String firstName, String lastName, String email, TimeZone timeZone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.timeZone = timeZone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
}
