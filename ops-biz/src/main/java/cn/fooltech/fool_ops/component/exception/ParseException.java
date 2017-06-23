package cn.fooltech.fool_ops.component.exception;

public class ParseException extends RuntimeException {

    private static final long serialVersionUID = 5532279692163114451L;

    public ParseException() {
        super();
    }

    public ParseException(String msg) {
        super(msg);
    }

    public ParseException(Throwable t) {
        super(t);
    }

    public ParseException(String msg, Throwable t) {
        super(msg, t);
    }
}
