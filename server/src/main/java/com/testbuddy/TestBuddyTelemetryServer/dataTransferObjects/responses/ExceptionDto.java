package com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.responses;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDto {

    private Integer errorCode;
    private String message;

}
