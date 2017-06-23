package cn.fooltech.fool_ops.component.exception;

public class LoginTimeOutException extends RuntimeException {

    private static final long serialVersionUID = 5341441867487361209L;

    public LoginTimeOutException() {
        super();
    }

    public LoginTimeOutException(String msg) {
        super(msg);
    }

    public LoginTimeOutException(Throwable t) {
        super(t);
    }

    public LoginTimeOutException(String msg, Throwable t) {
        super(msg, t);
    }
}
