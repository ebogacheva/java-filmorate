package ru.yandex.practicum.filmorate.model.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UserLoginValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLoginValidation {
    String message() default "Invalid user login.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
