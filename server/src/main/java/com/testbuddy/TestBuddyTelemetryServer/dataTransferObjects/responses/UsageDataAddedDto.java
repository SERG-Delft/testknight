package com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.responses;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsageDataAddedDto {
    private String message = "Successfully added usage data";
}
