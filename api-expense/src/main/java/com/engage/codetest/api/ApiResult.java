package com.engage.codetest.api;

import com.fasterxml.jackson.annotation.JsonProperty;

// Generic class to represent results returned from API resources
// T is the actual data we want to return
public class ApiResult<T> {
    private boolean isOk;

    // if isOk
    private T data;
    // if !isOk
    private ErrorResult error;

    public ApiResult() {
        // Jackson deserialization
    }

    public ApiResult(T data) {
        this.isOk = true;
        this.data = data;
    }

    public ApiResult(String errorDescription, String errorCode) {
        this.isOk = false;
        this.error = new ErrorResult(errorDescription, errorCode);
    }

    @JsonProperty
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @JsonProperty
    public ErrorResult getError() {
        return error;
    }

    public void setError(ErrorResult error) {
        this.error = error;
    }

    @JsonProperty
    public boolean getIsOk() {
        return isOk;
    }

    public void setIsOk(boolean isOk) {
        this.isOk = isOk;
    }
}
