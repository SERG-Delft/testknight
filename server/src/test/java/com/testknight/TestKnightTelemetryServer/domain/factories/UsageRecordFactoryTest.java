package com.testknight.TestKnightTelemetryServer.domain.factories;

import com.testknight.TestKnightTelemetryServer.dataTransferObjects.requests.*;
import com.testknight.TestKnightTelemetryServer.domain.model.*;
import com.testknight.TestKnightTelemetryServer.exceptions.*;
import org.junit.jupiter.api.*;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UsageRecordFactoryTest {

    private final UsageRecordFactory usageRecordFactory = new UsageRecordFactory();

    private UsageDataDto usageDataDto;
    private final String userId = "userId";
    private final LocalDateTime time = LocalDateTime.now();
    private final String hash = "hash";
    private final String actionId = "testAdd";

    @BeforeEach
    public void setup() {
        ActionEventDto actionEventDto = new ActionEventDto(actionId, time);
        List<ActionEventDto> actionEventDtoList = new ArrayList<>();
        actionEventDtoList.add(actionEventDto);
        usageDataDto = new UsageDataDto(userId, actionEventDtoList, hash);
    }

    @Test
    public void testCreateFromDto() {
        UsageRecord expected = new UsageRecord(userId, new Action(actionId), time);
        UsageRecord actual = usageRecordFactory.createUsageRecordFromDto(usageDataDto).get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateFromDtoEmptyList() {
        usageDataDto.setActionsRecorded(new ArrayList<>());
        List<UsageRecord> actualList = usageRecordFactory.createUsageRecordFromDto(usageDataDto);
        assertEquals(0, actualList.size());
    }

    @Test
    public void testNullUserId() {
        usageDataDto.setUserId(null);
        assertThrows(NullFieldException.class, () -> {
            usageRecordFactory.createUsageRecordFromDto(usageDataDto);
        });
    }

    @Test
    public void testNullHash() {
        usageDataDto.setHash(null);
        assertThrows(NullFieldException.class, () -> {
            usageRecordFactory.createUsageRecordFromDto(usageDataDto);
        });
    }

    @Test
    public void testNullActions() {
        usageDataDto.setActionsRecorded(null);
        assertThrows(NullFieldException.class, () -> {
            usageRecordFactory.createUsageRecordFromDto(usageDataDto);
        });
    }

}