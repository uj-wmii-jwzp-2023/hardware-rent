package uj.wmii.jwzp.hardwarerent.exceptions;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message){
         super(message);
    }
}

