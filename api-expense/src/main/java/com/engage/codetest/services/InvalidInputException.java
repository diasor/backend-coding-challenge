package com.engage.codetest.services;

/**
 * @author Diana Sormani
 * Created: February 03, 2018
 * Last Updated: February 05, 2018
 * Description: The InvalidInputException class was created to define a particular kind of exception:
 *              indicates when a data input is invalid or incorrect somehow.
 *              It extends the RunTimeException
 */
public class InvalidInputException extends RuntimeException {
    private final String inputCode = "INVALID_INPUT";
    private String inputName;       // Name of the input which has failed validation (ex: expenseDate, expenseAmount, etc)
    private String reason;          // Reason for failing validation: null value, not a date, etc.

    public InvalidInputException(String inputName, String reason) {
        this.inputName = inputName;
        this.reason = reason;
    }

    public String getDescription() {
        return "Input: " + this.inputName + ": " + this.reason;
    }

    public String getErrorCode() {
        return this.inputCode;
    }

}
