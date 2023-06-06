package uj.wmii.jwzp.hardwarerent.exceptions;

public class NoEnoughMoneyException extends RuntimeException{
    public NoEnoughMoneyException() {
        super();
    }

    public NoEnoughMoneyException(String message) {
        super(message);
    }

    public NoEnoughMoneyException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoEnoughMoneyException(Throwable cause) {
        super(cause);
    }

    protected NoEnoughMoneyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
