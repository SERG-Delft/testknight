package com.testknight.TestBuddyTelemetryServer.dataTransferObjects.requests;

import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class UsageDataDto implements RequestDto {

    private String userId;
    private List<ActionEventDto> actionsRecorded;
    private String hash;

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
        StringBuilder builder = new StringBuilder();
        builder.append(userId);
        if (actionsRecorded != null) {
            for (ActionEventDto actionEventDto : actionsRecorded) {
                builder.append(actionEventDto.toHashString());
            }
        }
        return builder.toString();
    }
}
