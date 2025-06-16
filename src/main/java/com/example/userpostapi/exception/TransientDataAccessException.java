package com.example.userpostapi.exception;

public class TransientDataAccessException extends DataAccessException {
    public TransientDataAccessException(String message) { super(message); }
}
