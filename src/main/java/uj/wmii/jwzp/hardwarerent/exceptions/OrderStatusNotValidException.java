package uj.wmii.jwzp.hardwarerent.exceptions;

public class OrderStatusNotValidException extends RuntimeException {

    public OrderStatusNotValidException() {
        super();
    }

    public OrderStatusNotValidException(String message) {
        super(message);
    }

    public OrderStatusNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderStatusNotValidException(Throwable cause) {
        super(cause);
    }

    protected OrderStatusNotValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
