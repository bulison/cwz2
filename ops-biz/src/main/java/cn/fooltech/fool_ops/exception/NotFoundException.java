package cn.fooltech.fool_ops.exception;


public class NotFoundException extends BusinessException {
    public NotFoundException(String message) {
        super(message, 404);
    }
}
