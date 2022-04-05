package com.spd.trello.annotation;

import com.spd.trello.repository_jpa.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private UserRepository repository;

    public EmailValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return repository.findUserByEmail(value).isEmpty();
    }
}
