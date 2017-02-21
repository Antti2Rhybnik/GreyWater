package com.greywater.iot.rest;


public class RandomServerException extends Exception {

    public RandomServerException(String message) {
        super(message);
    }

    public RandomServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
