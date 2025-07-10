package com.idiot9.ldap.exceptions;

public class IncorrectParamsException extends RuntimeException {
    public IncorrectParamsException(){
        super();
    }
    public IncorrectParamsException(String message){
        super(message);
    }
}
