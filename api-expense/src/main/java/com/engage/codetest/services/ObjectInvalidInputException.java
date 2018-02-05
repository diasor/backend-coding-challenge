package com.engage.codetest.services;

public class ObjectInvalidInputException extends RuntimeException {
    private String inputName;
    private String reason;

    public ObjectInvalidInputException(String inputName, String reason) {
        this.inputName = inputName;
        this.reason = reason;
    }

    public String getDescription() {
        return "Invalid Input";
    }

    public String getErrorCode() {
        return "INV_INPUT";
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }
}
