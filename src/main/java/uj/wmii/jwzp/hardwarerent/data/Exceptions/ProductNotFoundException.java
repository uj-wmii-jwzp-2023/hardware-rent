package uj.wmii.jwzp.hardwarerent.data.Exceptions;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message){
         super(message);
    }
}

