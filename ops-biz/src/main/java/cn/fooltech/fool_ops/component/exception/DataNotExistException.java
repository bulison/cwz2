package cn.fooltech.fool_ops.component.exception;

/**
 * 数据不存在的异常
 *
 * @author xjh
 */
public class DataNotExistException extends RuntimeException {

    private static final long serialVersionUID = 8078345895046316131L;

    public DataNotExistException() {
        super();
    }

    public DataNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataNotExistException(String message) {
        super(message);
    }

    public DataNotExistException(Throwable cause) {
        super(cause);
    }

}
