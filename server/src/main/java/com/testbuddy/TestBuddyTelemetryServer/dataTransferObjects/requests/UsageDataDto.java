package com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.requests;

import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class UsageDataDto {

    private String userId;
    private List<ActionEventDto> actionsRecorded;
    private String hash;

}
