package com.testknight.TestKnightTelemetryServer.dataTransferObjects.requests;


import lombok.Data;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ActionEventDto implements RequestDto {

    private String actionId;
    private LocalDateTime dateTime;

    /**
     * Creates a string representation
     * of the DTO that is suitable for
     * hashing.
     *
     * @return the string that can be used
     *         to generate a hash.
     */
    @Override
    public String toHashString() {
        return actionId + dateTime.toString();
    }
}
