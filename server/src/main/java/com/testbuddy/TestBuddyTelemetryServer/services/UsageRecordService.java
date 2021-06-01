package com.testbuddy.TestBuddyTelemetryServer.services;

import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.requests.*;
import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.responses.*;
import com.testbuddy.TestBuddyTelemetryServer.factories.*;
import com.testbuddy.TestBuddyTelemetryServer.model.*;
import com.testbuddy.TestBuddyTelemetryServer.repositories.*;
import com.testbuddy.TestBuddyTelemetryServer.security.*;
import com.testbuddy.TestBuddyTelemetryServer.validation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class UsageRecordService {

    private final UsageRecordRepository usageRecordRepository;
    private final UsageRecordFactory usageRecordFactory;
    private final ActionRepository actionRepository;

    @Autowired
    public UsageRecordService(UsageRecordRepository usageRecordRepository, UsageRecordFactory usageRecordFactory,
                              ActionRepository actionRepository) {
        this.usageRecordRepository = usageRecordRepository;
        this.usageRecordFactory = usageRecordFactory;
        this.actionRepository = actionRepository;
    }

    /**
     * Saves the given usage data in the database.
     *
     * @param usageDataDto the DTO with the data that needs to be stored.
     * @return a UsageDataAddedDto meaning that the data got successfully persisted.
     */
    public UsageDataAddedDto persistUsageData(UsageDataDto usageDataDto) {
        RequestValidator<UsageDataDto> validator = createValidationChain();
        validator.handle(usageDataDto);
        List<UsageRecord> usageRecords = usageRecordFactory.createUsageRecordFromDto(usageDataDto);
        usageRecordRepository.saveAll(usageRecords);
        return new UsageDataAddedDto();
    }

    /**
     * Creates a validation chain for the UsageDataDto objects.
     *
     * @return a request validator representing the first validator
     * in the chain.
     */
    private RequestValidator<UsageDataDto> createValidationChain() {
        HashValidator hashValidator = new HashValidator(new Md5Hasher());
        ContentValidator contentValidator = new ContentValidator(actionRepository);
        hashValidator.setNext(contentValidator);
        return hashValidator;
    }

}
