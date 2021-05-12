package com.testbuddy.models

import com.testbuddy.com.testbuddy.models.TruthTable
import org.junit.Test
import kotlin.test.assertEquals

internal class TruthTableTest {

    @Test
    fun testBasic() {
        val table = TruthTable(listOf("a", "b"), "a && b")

        assertEquals(table.value(0), 0)
        assertEquals(table.value(1), 0)
        assertEquals(table.value(2), 0)
        assertEquals(table.value(3), 1)
    }

    @Test
    fun testAssignments() {
        val table = TruthTable(listOf("a", "b"), "a && b")

        val assignments = table.assignments(3)

        assertEquals(assignments["a"], true)
        assertEquals(assignments["b"], true)
    }
}
