package com.testbuddy.TestBuddyTelemetryServer.validation;

import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.requests.*;
import com.testbuddy.TestBuddyTelemetryServer.exceptions.*;
import com.testbuddy.TestBuddyTelemetryServer.security.*;

public class HashValidator extends BaseValidator<UsageDataDto> {

    private final Hasher hasher;
    private final String magicString = "exampleMagicString";

    public HashValidator(Hasher hasher) {
        this.hasher = hasher;
    }

    /**
     * Handles the request by checking if the hash
     * was generated in a verified client.
     *
     * @param requestDto the request to be handled.
     * @throws ValidationException thrown iff the request is invalid.
     */
    @Override
    public void handle(UsageDataDto requestDto) throws ValidationException {
        String hashedContents = hasher.hash(requestDto.toHashString() + magicString);
        if (!hashedContents.equals(requestDto.getHash())) {
            throw new InvalidHashException();
        }
        super.handle(requestDto);
    }

}
