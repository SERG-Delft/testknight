package com.testknight.TestKnightTelemetryServer.dataTransferObjects.requests;

public interface RequestDto {

    /**
     * Creates a string representation
     * of the DTO that is suitable for
     * hashing.
     *
     * @return the string that can be used
     *         to generate a hash.
     */
    public String toHashString();
}
