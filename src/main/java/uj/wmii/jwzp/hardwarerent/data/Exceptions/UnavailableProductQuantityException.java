package uj.wmii.jwzp.hardwarerent.data.Exceptions;

public class UnavailableProductQuantityException extends RuntimeException{
    public UnavailableProductQuantityException(String message){
        super(message);
    }
}
