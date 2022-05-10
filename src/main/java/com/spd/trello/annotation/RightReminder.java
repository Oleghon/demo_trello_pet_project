package com.spd.trello.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RightReminderValidator.class)
public @interface RightReminder {

    String message() default "reminder not correct";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
