package com.testknight.models

import org.junit.Test
import kotlin.test.assertEquals

class ActionDataTest {

    @Test
    fun testHashString() {
        val actionData = ActionData("actionId")
        assertEquals(actionData.toHashString(), "actionId${actionData.dateTime}")
    }
}
