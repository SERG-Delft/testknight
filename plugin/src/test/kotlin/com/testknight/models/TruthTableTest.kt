package com.testknight.models

import org.junit.Test
import kotlin.test.assertEquals

internal class TruthTableTest {

    @Test
    fun testBasic() {
        val table = TruthTable(listOf("a", "b"), "a && b")

        assertEquals(table.value(0), false)
        assertEquals(table.value(1), false)
        assertEquals(table.value(2), false)
        assertEquals(table.value(3), true)
    }

    @Test
    fun testNonlazy() {
        val table = TruthTable(listOf("a", "b"), "a & b")

        assertEquals(table.value(0), false)
        assertEquals(table.value(1), false)
        assertEquals(table.value(2), false)
        assertEquals(table.value(3), true)
    }

    @Test
    fun testXor() {
        val table = TruthTable(listOf("a", "b"), "a ^ b")

        assertEquals(table.value(0), false)
        assertEquals(table.value(1), true)
        assertEquals(table.value(2), true)
        assertEquals(table.value(3), false)
    }

    @Test
    fun testAssignments() {
        val table = TruthTable(listOf("a", "b"), "a && b")

        val assignments = table.assignments(3)

        assertEquals(assignments["a"], true)
        assertEquals(assignments["b"], true)
    }
}
