package com.testbuddy.TestBuddyTelemetryServer.exceptions;

public class NullFieldException extends ValidationException {

    public static final long serialVersionUID = 2567201903468237233L;

    /**
     * Constructs a new NullFieldException
     * @param nameOfField the name of the null field.
     */
    public NullFieldException(String nameOfField) {
        super("Field " + nameOfField + " cannot be null");
    }

}
