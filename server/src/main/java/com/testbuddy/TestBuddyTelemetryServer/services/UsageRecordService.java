package com.testbuddy.TestBuddyTelemetryServer.services;

import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.requests.*;
import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.responses.*;
import org.springframework.stereotype.*;

@Service
public class UsageRecordService {

    public UsageDataAddedDto persistUsageData(UsageDataDto usageDataDto) {
        return new UsageDataAddedDto();
    }

}
