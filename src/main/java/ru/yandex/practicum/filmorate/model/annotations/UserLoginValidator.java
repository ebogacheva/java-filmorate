package ru.yandex.practicum.filmorate.model.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserLoginValidator implements ConstraintValidator<UserLoginValidation, String> {
    @Override
    public void initialize(UserLoginValidation userLogin) {
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        if (login == null) {
            return false;
        } else return login.chars().noneMatch(Character::isWhitespace);
    }
}
