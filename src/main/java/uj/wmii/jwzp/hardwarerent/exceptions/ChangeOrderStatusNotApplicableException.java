package uj.wmii.jwzp.hardwarerent.exceptions;

public class ChangeOrderStatusNotApplicableException extends RuntimeException {
    public ChangeOrderStatusNotApplicableException() {
        super();
    }

    public ChangeOrderStatusNotApplicableException(String message) {
        super(message);
    }

    public ChangeOrderStatusNotApplicableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChangeOrderStatusNotApplicableException(Throwable cause) {
        super(cause);
    }

    protected ChangeOrderStatusNotApplicableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
