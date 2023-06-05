package uj.wmii.jwzp.hardwarerent.controllers;

public class CategoryRemovalException extends RuntimeException{
    public CategoryRemovalException() {
        super();
    }

    public CategoryRemovalException(String message) {
        super(message);
    }

    public CategoryRemovalException(String message, Throwable cause) {
        super(message, cause);
    }

    public CategoryRemovalException(Throwable cause) {
        super(cause);
    }

    protected CategoryRemovalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
