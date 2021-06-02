package com.testbuddy.TestBuddyTelemetryServer.controllers;

import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.requests.*;
import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.responses.*;
import com.testbuddy.TestBuddyTelemetryServer.exceptions.*;
import com.testbuddy.TestBuddyTelemetryServer.services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usagedata")
public class UsageDataController {

    private final UsageRecordService usageDataService;

    @Autowired
    public UsageDataController(UsageRecordService usageDataService) {
        this.usageDataService = usageDataService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsageDataAddedDto addUsageData(@RequestBody UsageDataDto usageData) {
        return usageDataService.persistUsageData(usageData);
    }

    @ExceptionHandler(value = InvalidActionId.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto invalidActionIdHandler(InvalidActionId exception) {
        return new ExceptionDto(400, exception.getMessage());
    }

    @ExceptionHandler(value = NullFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto nullFieldHandler(NullFieldException exception) {
        return new ExceptionDto(400, exception.getMessage());
    }

    @ExceptionHandler(value = InvalidHashException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionDto invalidHashHandler(InvalidHashException exception) {
        return new ExceptionDto(401, exception.getMessage());
    }
}
