package com.example.userpostapi.exception;

public class OperationFailedException extends RuntimeException {
    public OperationFailedException(String message) { super(message); }
}