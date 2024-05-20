package com.khaphp.foodreceipeservice.exception;

public class ObjectNotFound extends RuntimeException{
    public ObjectNotFound(String message) {
        super(message);
    }
}
