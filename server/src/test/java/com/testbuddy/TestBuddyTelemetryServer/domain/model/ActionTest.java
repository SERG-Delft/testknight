package com.testbuddy.TestBuddyTelemetryServer.domain.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ActionTest {

    @Test
    void testGetActionId() {
        Action action = new Action("actionId");
        assertEquals("actionId", action.getActionId());
    }

    @Test
    void testSetActionId() {
        Action action = new Action();
        action.setActionId("newActionId");
        assertEquals("newActionId", action.getActionId());
    }

    @Test
    void testEqualsTrue() {
        Action actionOne = new Action("actionId");
        Action actionTwo = new Action("actionId");
        assertEquals(actionTwo, actionOne);
    }

    @Test
    void testEqualsSelf() {
        Action actionOne = new Action("actionId");
        assertEquals(actionOne, actionOne);
    }

    @Test
    void testEqualsFalse() {
        Action actionOne = new Action("actionId");
        Action actionTwo = new Action("otherActionId");
        assertNotEquals(actionTwo, actionOne);
    }

    @Test
    void testEqualsOtherType() {
        Action actionOne = new Action("actionId");
        UsageRecord usageRecord = new UsageRecord();
        assertNotEquals(actionOne, usageRecord);
    }

    @Test
    void testEqualsWithNull() {
        Action actionOne = new Action("actionId");
        assertNotEquals(actionOne, null);
    }

    @Test
    void testCanEqual() {
        Action actionOne = new Action("actionId");
        Action actionTwo = new Action("actionId");
        assertTrue(actionOne.canEqual(actionTwo));
    }

    @Test
    void testToString() {
        Action action = new Action("actionId");
        assertEquals("Action(actionId=actionId)", action.toString());
    }
}