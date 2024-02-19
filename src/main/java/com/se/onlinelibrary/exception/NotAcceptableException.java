package com.se.onlinelibrary.exception;

public class NotAcceptableException extends RuntimeException{
    private static final long serialVersionUID = -6074753940710869977L;
    public NotAcceptableException(String message) {
        super(message);
    }
}
