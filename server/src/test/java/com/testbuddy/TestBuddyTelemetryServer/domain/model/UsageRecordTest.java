package com.testbuddy.TestBuddyTelemetryServer.domain.model;

import org.junit.jupiter.api.*;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class UsageRecordTest {

    LocalDateTime localDateTime = LocalDateTime.now();
    UsageRecord usageRecord;

    @BeforeEach
    void setup() {
        usageRecord = new UsageRecord(1L, "userId", new Action("actionId"), localDateTime);
    }

    @Test
    void testGetId() {
        assertEquals(1L, usageRecord.getId());
    }

    @Test
    void testGetUserId() {
        assertEquals("userId", usageRecord.getUserId());
    }

    @Test
    void testGetActionId() {
        assertEquals(new Action("actionId"), usageRecord.getActionId());
    }

    @Test
    void testGetDateTime() {
        assertEquals(localDateTime, usageRecord.getDateTime());
    }

    @Test
    void testSetId() {
        usageRecord.setId(2L);
        assertEquals(2L, usageRecord.getId());
    }

    @Test
    void testSetUserId() {
        usageRecord.setUserId("newUserId");
        assertEquals("newUserId", usageRecord.getUserId());
    }

    @Test
    void testSetActionId() {
        Action newAction = new Action("newActionId");
        usageRecord.setActionId(newAction);
        assertEquals(newAction, usageRecord.getActionId());
    }

    @Test
    void testSetDateTime() {
        LocalDateTime newDate = LocalDateTime.now();
        usageRecord.setDateTime(newDate);
        assertEquals(newDate, usageRecord.getDateTime());
    }

    @Test
    void testEqualsTrue() {
        UsageRecord usageRecord1 = new UsageRecord(1L, "userId", new Action("actionId"), localDateTime);
        assertEquals(usageRecord, usageRecord1);
    }

    @Test
    void testEqualsSame() {
        assertEquals(usageRecord, usageRecord);
    }

    @Test
    void testEqualsOtherUserId() {
        UsageRecord usageRecord1 = new UsageRecord(1L, "otherUserId", new Action("actionId"), localDateTime);
        assertNotEquals(usageRecord1, usageRecord);
    }

    @Test
    void testEqualsOtherId() {
        UsageRecord usageRecord1 = new UsageRecord(2L, "userId", new Action("actionId"), localDateTime);
        assertNotEquals(usageRecord, usageRecord1);
    }

    @Test
    void testEqualsOtherActionId() {
        UsageRecord usageRecord1 = new UsageRecord(1L, "userId", new Action("otherActionId"), localDateTime);
        assertNotEquals(usageRecord, usageRecord1);
    }

    @Test
    void testEqualsOtherTime() {
        UsageRecord usageRecord1 = new UsageRecord(1L, "userId", new Action("actionId"), LocalDateTime.MAX);
        assertNotEquals(usageRecord, usageRecord1);
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(usageRecord, null);
    }

    @Test
    void testEqualsWithOtherClass() {
        Action action = new Action("actionId");
        assertNotEquals(usageRecord, action);
    }

    @Test
    void testCanEqual() {
        assertTrue(usageRecord.canEqual(usageRecord));
    }

    @Test
    void testToString() {
        String expected = "UsageRecord(id=1, userId=userId, actionId=Action(actionId=actionId), dateTime=" + localDateTime.toString() + ")";
        assertEquals(expected, usageRecord.toString());
    }
}