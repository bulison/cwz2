package cn.fooltech.fool_ops.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 验证工具
 * @author xjh
 *
 */
public class ValidatorUtils {

	private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	public static Validator getValidator(){
		return factory.getValidator();
	}
	
	/**
	 * 验证，成功返回null，否则返回错误信息
	 * @param t
	 * @return
	 */
	public static <T> String inValidMsg(T t){
		Set<ConstraintViolation<T>> set = getValidator().validate(t);   
        for (ConstraintViolation<T> constraintViolation : set){
        	return constraintViolation.getMessage();
        }
        return null;
	}
	
	/**
	 * 验证，成功返回null，否则返回错误信息
	 * @param t
	 * @return
	 */
	public static <T> String inValidMsg(T t,  Class<?>... groups){
		Set<ConstraintViolation<T>> set = getValidator().validate(t, groups);
        for (ConstraintViolation<T> constraintViolation : set){
        	return constraintViolation.getMessage();
        }
        return null;
	}
}
