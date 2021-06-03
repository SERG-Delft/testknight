package com.testbuddy.TestBuddyTelemetryServer.exceptions;

public class InvalidActionId extends ValidationException {

    public static final long serialVersionUID = 927626852988215140L;


    public InvalidActionId(String actionId) {
        super(actionId + " is not a valid actionId");
    }

}
