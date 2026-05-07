package com.example.agricole.exception;

public class CollectivityNotFoundException extends RuntimeException {
    public CollectivityNotFoundException(String id) {
        super("Collectivity.id=" + id + " is not found");
    }
}