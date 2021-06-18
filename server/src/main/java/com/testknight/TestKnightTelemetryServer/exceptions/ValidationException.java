package com.testknight.TestKnightTelemetryServer.exceptions;

public class ValidationException extends RuntimeException {

    public static final long serialVersionUID = 2336261092312323L;

    public ValidationException(String message) {
        super(message);
    }

}
