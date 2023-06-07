package uj.wmii.jwzp.hardwarerent.exceptions;

public class ProductAlreadyInOrderException extends RuntimeException {
    public ProductAlreadyInOrderException() {
        super();
    }

    public ProductAlreadyInOrderException(String message) {
        super(message);
    }

    public ProductAlreadyInOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductAlreadyInOrderException(Throwable cause) {
        super(cause);
    }

    protected ProductAlreadyInOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
