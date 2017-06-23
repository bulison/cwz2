package cn.fooltech.fool_ops.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IDCardValidator implements ConstraintValidator<IDCard, String> {

    @Override
    public void initialize(IDCard idCard) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) return true;
        return IDCardUtil.isIDCard(value);
    }

}
