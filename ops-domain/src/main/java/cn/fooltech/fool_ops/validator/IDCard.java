package cn.fooltech.fool_ops.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {IDCardValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IDCard {
    String message() default "非法身份证";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
