package com.testknight.TestKnightTelemetryServer.services;

import com.testknight.TestKnightTelemetryServer.dataTransferObjects.requests.*;
import com.testknight.TestKnightTelemetryServer.dataTransferObjects.responses.*;
import com.testknight.TestKnightTelemetryServer.domain.factories.*;
import com.testknight.TestKnightTelemetryServer.domain.model.*;
import com.testknight.TestKnightTelemetryServer.repositories.*;
import com.testknight.TestKnightTelemetryServer.security.*;
import com.testknight.TestKnightTelemetryServer.validation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class UsageRecordService {

    private final UsageRecordRepository usageRecordRepository;
    private final UsageRecordFactory usageRecordFactory;
    private final ActionRepository actionRepository;

    /**
     * Creates a new UsageRecordService.
     *
     * @param usageRecordRepository the repository that accesses usage records.
     * @param usageRecordFactory the factory that creates usage records.
     * @param actionRepository the repository that accesses actions.
     */
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
     *         in the chain.
     */
    private RequestValidator<UsageDataDto> createValidationChain() {
        HashValidator hashValidator = new HashValidator(new Md5Hasher());
        ContentValidator contentValidator = new ContentValidator(actionRepository);
        hashValidator.setNext(contentValidator);
        return hashValidator;
    }

}
