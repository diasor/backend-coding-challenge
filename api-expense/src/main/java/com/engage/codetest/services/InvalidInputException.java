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
    private String inputName;
    private String reason;

    public InvalidInputException(String inputName, String reason) {
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
