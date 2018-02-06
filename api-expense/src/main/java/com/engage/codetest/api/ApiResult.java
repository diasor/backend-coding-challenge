package com.engage.codetest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * @author Diana Sormani
 * Created: January 31, 2018
 * Last Updated: February 05, 2018
 * Description: The ApiResult class was created to interact directly with the frontend.
 *              It has three members:
 *                  - The first is a boolean indicating whether the service called was successful or not
 *                  - The second is an generic object intended to have an ExpenseJSON object containgin the expense
 *                  information with attributes named exactly as they were in the frontend. If the "isOk" field is true,
 *                  then the information retrieved can be found here.
 *                  - The third member, is an ErrorResult object, containing all the error information in case
 *                  the "isOk" field is false.
 */

// Generic class to represent results returned from API resources
// T is the actual data we want to return
public class ApiResult<T> {
    private boolean isOk;       // isOk indicates whether the service called was successful or not
    private T data;             // if isOk, then the information is stored in the data field
    private ErrorResult error;  // if !isOk, then the error is depicted in the error object

    public ApiResult() {
        // Do not remove Jackson deserialization
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
