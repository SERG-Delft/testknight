package com.testbuddy.TestBuddyTelemetryServer.controllers;


import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.requests.*;
import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.responses.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usagedata")
public class UsageDataController {

    @PostMapping
    public AddedUsageData addUsageData(UsageDataDto usageData) {
        return new AddedUsageData();
    }

}
