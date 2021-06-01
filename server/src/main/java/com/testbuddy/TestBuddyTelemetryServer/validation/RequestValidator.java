package com.testbuddy.TestBuddyTelemetryServer.validation;

import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.requests.*;
import com.testbuddy.TestBuddyTelemetryServer.exceptions.*;

/**
 * The request validator interface. To be implemented
 * by all classes of the request validation chain.
 *
 * @param <E> The type of RequestDto being validated.
 *            The generic here allows to customize the handle
 *            method as well as ensure that the next validator
 *            in the chain handles the same type of request.
 */
public interface RequestValidator<E extends RequestDto> {

    /**
     * Sets the next validator in the chain.
     *
     * @param nextValidator the validator to be used next.
     */
    public void setNext(RequestValidator<E> nextValidator);

    /**
     * Handles the request.
     *
     * @param requestDto the request to be handled.
     * @throws ValidationException thrown iff the request is invalid.
     */
    public void handle(E requestDto) throws ValidationException;

}
