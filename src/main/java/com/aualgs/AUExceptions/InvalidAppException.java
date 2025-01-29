package com.aualgs.AUExceptions;
public class InvalidAppException extends Exception { 
    public InvalidAppException() {
        super();
    }
    public InvalidAppException(String errorMessage) {
        super(errorMessage);
    }
}