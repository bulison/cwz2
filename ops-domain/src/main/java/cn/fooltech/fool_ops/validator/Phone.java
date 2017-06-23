package cn.fooltech.fool_ops.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {PhoneValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {
    String message() default "非法手机号码";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
