package com.testbuddy.checklistGenerationStrategies

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import org.junit.Before
import org.junit.Test

internal class ConditionChecklistGenerationStrategyTest : BasePlatformTestCase() {

    @Before
    override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testMcdc0prop() {
        val strategy = ConditionChecklistGenerationStrategy()

        val testCases = strategy.mcdc(listOf(), "true")

        assertEmpty(testCases)
    }

    @Test
    fun testMcdc1Prop() {
        val strategy = ConditionChecklistGenerationStrategy()

        val testCases = strategy.mcdc(listOf("a"), "a")

        assertContainsElements(
            testCases,
            mapOf("a" to true),
            mapOf("a" to false),
        )
        assertEquals(2, testCases.size)
    }

    @Test
    fun testMcdc2Props() {
        val strategy = ConditionChecklistGenerationStrategy()

        val testCases = strategy.mcdc(listOf("a", "b"), "a && b")

        assertContainsElements(
            testCases,
            mapOf("a" to true, "b" to false),
            mapOf("a" to true, "b" to true),
            mapOf("a" to false, "b" to true)
        )
        assertEquals(3, testCases.size)
    }

    @Test
    fun testMcdc3Props() {
        val strategy = ConditionChecklistGenerationStrategy()

        val testCases = strategy.mcdc(listOf("a", "b", "c"), "a && (b || c)")

        assertContainsElements(
            testCases,
            mapOf("a" to false, "b" to false, "c" to true),
            mapOf("a" to true, "b" to false, "c" to true),
            mapOf("a" to true, "b" to false, "c" to false),
            mapOf("a" to true, "b" to true, "c" to false)
        )
        assertEquals(4, testCases.size)
    }

    @Test
    fun testMcdc4Props() {
        val strategy = ConditionChecklistGenerationStrategy()

        val testCases = strategy.mcdc(listOf("a", "b", "c", "d"), "a && (b || c || d)")

        assertContainsElements(
            testCases,
            mapOf("a" to false, "b" to false, "c" to false, "d" to true),
            mapOf("a" to true, "b" to false, "c" to false, "d" to true),
            mapOf("a" to true, "b" to false, "c" to false, "d" to false),
            mapOf("a" to true, "b" to true, "c" to false, "d" to false),
            mapOf("a" to true, "b" to false, "c" to true, "d" to false)
        )
        assertEquals(5, testCases.size)
    }
}
