package com.testknight.TestBuddyTelemetryServer.exceptions;

public class InvalidActionIdException extends ValidationException {

    public static final long serialVersionUID = 927626852988215140L;


    public InvalidActionIdException(String actionId) {
        super(actionId + " is not a valid actionId");
    }

}
