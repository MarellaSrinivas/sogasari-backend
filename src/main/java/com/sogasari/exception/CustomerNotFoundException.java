package com.sogasari.exception;

public class CustomerNotFoundException
        extends RuntimeException {

    public CustomerNotFoundException(
            String message
    ) {
        super(message);
    }
}