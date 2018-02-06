package com.engage.codetest.services;

/**
 * @author Diana Sormani
 * Created: January 31, 2018
 * Last Updated: February 05, 2018
 * Description: The ObjectNotFoundException class was created to define a particular kind of exception:
 *              indicates when a requested object can not be found.
 *              It extends the RunTimeException
 */
public class ObjectNotFoundException extends RuntimeException {
    private int requestedId;
    private String objectType;

    public ObjectNotFoundException(int requestedId, String objectType) {
        this.requestedId = requestedId;
        this.objectType = objectType;
    }

    public String getDescription() {
        return "The Object identified with: " + this.requestedId + " was not found";
    }

    public String getErrorCode() {
        return "NOT_FOUND";
    }

    public int getRequestedId() {
        return this.requestedId;
    }

    public void setRequestedId(int requestedId) {
        this.requestedId = requestedId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
}
