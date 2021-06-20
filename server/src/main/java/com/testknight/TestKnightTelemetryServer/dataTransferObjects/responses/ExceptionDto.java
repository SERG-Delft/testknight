package com.testknight.TestKnightTelemetryServer.dataTransferObjects.responses;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDto {

    private Integer errorCode;
    private String message;

}
