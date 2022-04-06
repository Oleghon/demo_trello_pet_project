package com.spd.trello.annotation;

import com.spd.trello.domain.items.Reminder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RightReminderValidator implements ConstraintValidator<RightReminder, Reminder> {

    @Override
    public boolean isValid(Reminder value, ConstraintValidatorContext context) {
        if (value.getEnd() == null && value.getRemindOn() != null || value.getStart() == null)
            return false;
        if (value.getEnd() != null && value.getRemindOn() == null)
            return value.getStart().isBefore(value.getEnd());
        if (value.getEnd() == null && value.getRemindOn() == null)
            return true;

        return value.getStart().isBefore(value.getEnd())
                && value.getStart().isBefore(value.getRemindOn())
                && value.getRemindOn().isBefore(value.getEnd());
    }
}
