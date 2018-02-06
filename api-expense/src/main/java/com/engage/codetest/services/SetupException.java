package com.engage.codetest.services;

/**
 * @author Diana Sormani
 * Created: February 05, 2018
 * Last Updated: February 05, 2018
 * Description: The SetupException class was created to define a particular kind of exception:
 *              indicates when some kind of application setup, configuration or initialization
 *              failed.
 *              It extends the RunTimeException
 */
public class SetupException extends RuntimeException {
    private final String setupCode = "SETUP_FAILED";
    private String setupObject;         // Setup object which failed
    private String setupFailureDsc;     // Failure description

    public SetupException(String setupObject, String description) {
        this.setupObject = setupObject;
        this.setupFailureDsc = description;
    }

    public String getDescription() {
        return this.setupFailureDsc;
    }

    public String getErrorCode() {
        return this.setupCode;
    }

}
