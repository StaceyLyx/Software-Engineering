package com.se.onlinelibrary.exception;

public class CurrentUserHasRegistered extends RuntimeException{
    private static final long serialVersionUID = -6074753940710869977L;
    public CurrentUserHasRegistered(String message) {
        super(message);
    }
}
