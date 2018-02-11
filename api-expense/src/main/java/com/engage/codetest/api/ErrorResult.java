package com.engage.codetest.api;

public class ErrorResult {
    private String errorDescription;
    private String errorCode;

    public ErrorResult(String errorDescription, String errorCode) {
        this.errorDescription = errorDescription;
        this.errorCode = errorCode;
    }

    public  ErrorResult(){
        // Do not REMOVE. Created for  testing with mockito purposes
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
