package uj.wmii.jwzp.hardwarerent.exceptions;

public class OrderDetailToOrderNotAddableException extends RuntimeException {
    public OrderDetailToOrderNotAddableException() {
        super();
    }

    public OrderDetailToOrderNotAddableException(String message) {
        super(message);
    }

    public OrderDetailToOrderNotAddableException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderDetailToOrderNotAddableException(Throwable cause) {
        super(cause);
    }

    protected OrderDetailToOrderNotAddableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
