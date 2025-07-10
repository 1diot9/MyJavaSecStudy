package com.idiot9.ldap.exceptions;

public class UnSupportedPayloadTypeException extends RuntimeException {
    public UnSupportedPayloadTypeException(){
        super();
    }
    public UnSupportedPayloadTypeException(String message){
        super(message);
    }
}
