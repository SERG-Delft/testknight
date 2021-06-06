package com.testbuddy.TestBuddyTelemetryServer.exceptions;

public class InvalidHashException extends ValidationException {

    public static final long serialVersionUID = 12162635991005290L;

    public InvalidHashException() {
        super("The hash included in the request does not come from a verified client");
    }

}
