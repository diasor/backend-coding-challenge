package com.engage.codetest.services;

/**
 * @author Diana Sormani
 * Created: February 05, 2018
 * Last Updated: February 05, 2018
 * Description: The SetUpException class was created to define a particular kind of exception:
 *              indicates when some kind of application setup, configuration or initialization
 *              failed.
 *              It extends the RunTimeException
 */
public class SetUpException extends RuntimeException {
    private String setUpObject;
    private String setUpFailureDsc;

    public SetUpException(String setUpObject, String description) {
        this.setUpObject = setUpObject;
        this.setUpFailureDsc = description;
    }

    public String getDescription() {
        return this.setUpFailureDsc;
    }

    public String getErrorCode() {
        return "SETUP_FAILED";
    }

    public String getsetUpObject() {
        return this.setUpObject;
    }

    public void setsetUpObject(String setUpObject) {
        this.setUpObject = setUpObject;
    }
}
