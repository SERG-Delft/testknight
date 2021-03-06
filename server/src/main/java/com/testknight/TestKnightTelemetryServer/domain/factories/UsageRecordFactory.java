package com.testknight.TestKnightTelemetryServer.domain.factories;

import com.testknight.TestKnightTelemetryServer.dataTransferObjects.requests.*;
import com.testknight.TestKnightTelemetryServer.exceptions.*;
import com.testknight.TestKnightTelemetryServer.domain.model.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class UsageRecordFactory {

    /**
     * Creates a list of UsageRecords from a UsageDataDto.
     *
     * @param usageDataDto the DTO to create from.
     * @return a list of UsageRecord objects.
     */
    public List<UsageRecord> createUsageRecordFromDto(UsageDataDto usageDataDto) {
        validateNonNullFields(usageDataDto);
        ArrayList<UsageRecord> usageRecords = new ArrayList<>();
        for (ActionEventDto actionEventDto : usageDataDto.getActionsRecorded()) {
            usageRecords.add(
                    new UsageRecord(
                            usageDataDto.getUserId(),
                            new Action(actionEventDto.getActionId()),
                            actionEventDto.getDateTime()
                    )
            );
        }
        return usageRecords;
    }

    private void validateNonNullFields(UsageDataDto dto) throws NullFieldException {
        if (dto.getUserId() == null) {
            throw new NullFieldException("userId");
        } else if (dto.getHash() == null) {
            throw new NullFieldException("hash");
        } else if (dto.getActionsRecorded() == null) {
            throw new NullFieldException("actionsRecorded");
        }
    }

}
