package com.testknight.models

import com.testknight.extensions.TestKnightTestCase
import com.testknight.settings.SettingsService
import org.junit.Test

class UsageDataTest : TestKnightTestCase() {

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
