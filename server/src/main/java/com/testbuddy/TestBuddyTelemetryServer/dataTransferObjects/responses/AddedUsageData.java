package com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.responses;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddedUsageData {
    private String message = "Successfully added usage data";
}
