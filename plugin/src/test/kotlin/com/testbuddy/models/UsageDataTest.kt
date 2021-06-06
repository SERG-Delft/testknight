package com.testbuddy.models

import com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.settings.SettingsService
import org.junit.Test

class UsageDataTest : TestBuddyTestCase() {

    @Test
    fun testToHashString() {
        val userId = SettingsService.instance.state.userId
        val a1 = ActionData("action1")
        val a2 = ActionData("action2")
        val usageData = UsageData(listOf(a1, a2))
        val hashString = usageData.toHashString()
        val expected = "${userId}${a1.toHashString()}${a2.toHashString()}"
        assertEquals(hashString, expected)
    }
}
