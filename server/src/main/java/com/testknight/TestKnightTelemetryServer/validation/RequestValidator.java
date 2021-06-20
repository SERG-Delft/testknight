package com.testknight.TestKnightTelemetryServer.validation;

import com.testknight.TestKnightTelemetryServer.dataTransferObjects.requests.*;
import com.testknight.TestKnightTelemetryServer.exceptions.*;

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
