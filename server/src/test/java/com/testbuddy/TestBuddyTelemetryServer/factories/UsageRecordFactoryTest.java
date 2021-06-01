package com.testbuddy.TestBuddyTelemetryServer.factories;

import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.requests.*;
import com.testbuddy.TestBuddyTelemetryServer.model.*;
import org.junit.jupiter.api.*;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UsageRecordFactoryTest {

    private final UsageRecordFactory usageRecordFactory = new UsageRecordFactory();

    private UsageDataDto usageDataDto;
    private final String USER_ID = "userId";
    private final LocalDateTime TIME = LocalDateTime.now();
    private final String HASH = "hash";
    private final String ACTION_ID = "testAdd";

    @BeforeEach
    public void setup() {
        ActionEventDto actionEventDto = new ActionEventDto(ACTION_ID, TIME);
        List<ActionEventDto> actionEventDtoList = new ArrayList<>();
        actionEventDtoList.add(actionEventDto);
        usageDataDto = new UsageDataDto(USER_ID, actionEventDtoList, HASH);
    }

    @Test
    public void testCreateFromDto() {
        UsageRecord expected = new UsageRecord(USER_ID, ACTION_ID, TIME);
        UsageRecord actual = usageRecordFactory.createUsageRecordFromDto(usageDataDto).get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateFromDtoEmptyList() {
        usageDataDto.setActionsRecorded(new ArrayList<>());
        List<UsageRecord> actualList = usageRecordFactory.createUsageRecordFromDto(usageDataDto);
        assertEquals(0, actualList.size());
    }

}