package com.testknight.TestBuddyTelemetryServer.validation;

import com.testknight.TestBuddyTelemetryServer.dataTransferObjects.requests.*;
import com.testknight.TestBuddyTelemetryServer.exceptions.*;

public abstract class BaseValidator<E extends RequestDto> implements RequestValidator<E> {

    private RequestValidator<E> next;

    /**
     * Sets the next validator in the chain.
     *
     * @param nextValidator the validator to be used next.
     */
    @Override
    public void setNext(RequestValidator<E> nextValidator) {
        this.next = nextValidator;
    }

    /**
     * Handles the request.
     *
     * @param requestDto the request to be handled.
     * @throws ValidationException thrown iff the request is invalid.
     */
    @Override
    public void handle(E requestDto) throws ValidationException {
        if (this.next != null) {
            next.handle(requestDto);
        }
    }
}
