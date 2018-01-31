package com.engage.codetest.services;

public class ObjectNotFoundException extends RuntimeException {
    private int requestedId;
    private String objectType;

    public ObjectNotFoundException(int requestedId, String objectType) {
        this.requestedId = requestedId;
        this.objectType = objectType;
    }

    public String getDescription() {
        return "Object not found";
    }

    public String getErrorCode() {
        return "NOT_FOUND";
    }

    public int getRequestedId() {
        return requestedId;
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
