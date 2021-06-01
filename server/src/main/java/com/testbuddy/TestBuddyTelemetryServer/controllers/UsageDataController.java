package com.testbuddy.TestBuddyTelemetryServer.controllers;

import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.requests.*;
import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.responses.*;
import com.testbuddy.TestBuddyTelemetryServer.services.*;
import org.springframework.beans.factory.annotation.*;
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
    public UsageDataAddedDto addUsageData(@RequestBody UsageDataDto usageData) {
        return usageDataService.persistUsageData(usageData);
    }

}
