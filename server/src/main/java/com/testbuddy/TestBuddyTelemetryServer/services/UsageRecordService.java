package com.testbuddy.TestBuddyTelemetryServer.services;

import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.requests.*;
import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.responses.*;
import com.testbuddy.TestBuddyTelemetryServer.factories.*;
import com.testbuddy.TestBuddyTelemetryServer.model.*;
import com.testbuddy.TestBuddyTelemetryServer.repositories.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class UsageRecordService {

    private final UsageRecordRepository usageRecordRepository;
    private final UsageRecordFactory usageRecordFactory;

    @Autowired
    public UsageRecordService(UsageRecordRepository usageRecordRepository, UsageRecordFactory usageRecordFactory) {
        this.usageRecordRepository = usageRecordRepository;
        this.usageRecordFactory = usageRecordFactory;
    }

    /**
     * Saves the given usage data in the database.
     *
     * @param usageDataDto the DTO with the data that needs to be stored.
     * @return a UsageDataAddedDto meaning that the data got successfully persisted.
     */
    public UsageDataAddedDto persistUsageData(UsageDataDto usageDataDto) {
        List<UsageRecord> usageRecords = usageRecordFactory.createUsageRecordFromDto(usageDataDto);
        usageRecordRepository.saveAll(usageRecords);
        return new UsageDataAddedDto();
    }

}
