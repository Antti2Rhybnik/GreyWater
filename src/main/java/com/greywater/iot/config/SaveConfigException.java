package com.greywater.iot.config;

public class SaveConfigException extends Exception {

    public SaveConfigException(String message) {
        super(message);
    }

    public SaveConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
