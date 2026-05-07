package com.example.agricole.exception;

public class AttendanceAlreadySetException extends RuntimeException {
    public AttendanceAlreadySetException(String message) {
        super(message);
    }
}
