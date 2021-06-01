package com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.requests;


import lombok.*;

import java.time.*;

@Data
@AllArgsConstructor
public class ActionEventDto {

    private String actionId;
    private LocalDateTime dateTime;

}
