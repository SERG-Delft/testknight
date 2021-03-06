package com.testknight.TestKnightTelemetryServer.controllers;

import com.testknight.TestKnightTelemetryServer.dataTransferObjects.requests.*;
import com.testknight.TestKnightTelemetryServer.dataTransferObjects.responses.*;
import com.testknight.TestKnightTelemetryServer.exceptions.*;
import com.testknight.TestKnightTelemetryServer.services.*;
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

    @ExceptionHandler(value = InvalidActionIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto invalidActionIdHandler(InvalidActionIdException exception) {
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
