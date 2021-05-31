package com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.exceptions.InvalidConfigurationException
import com.testbuddy.com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.ConditionChecklistNode
import junit.framework.TestCase
import org.junit.Test
import kotlin.test.assertFailsWith

internal class ConditionChecklistGenerationStrategyTest : TestBuddyTestCase() {

    @Test
    fun testMcdc0prop() {
        val strategy = ConditionChecklistGenerationStrategy.createWithMcDcConditionCoverage()

        val testCases = strategy.mcdc(listOf(), "true")

        assertEmpty(testCases)
    }

    @Test
    fun testMcdc1Prop() {
        val strategy = ConditionChecklistGenerationStrategy.createWithMcDcConditionCoverage()

        val testCases = strategy.mcdc(listOf("a"), "a").map { it.bindings }

        assertContainsElements(
            testCases,
            mapOf("a" to true),
            mapOf("a" to false),
        )
        assertEquals(2, testCases.size)
    }

    @Test
    fun testMcdc2Props() {
        val strategy = ConditionChecklistGenerationStrategy.createWithMcDcConditionCoverage()

        val testCases = strategy.mcdc(listOf("a", "b"), "a && b").map { it.bindings }

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
        val strategy = ConditionChecklistGenerationStrategy.createWithMcDcConditionCoverage()

        val testCases = strategy.mcdc(listOf("a", "b", "c"), "a && (b || c)").map { it.bindings }

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
        val strategy = ConditionChecklistGenerationStrategy.createWithMcDcConditionCoverage()

        val testCases = strategy.mcdc(listOf("a", "b", "c", "d"), "a && (b || c || d)")
            .map { it.bindings }

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

    @Test
    fun testBranchCoverage() {
        val strategy = ConditionChecklistGenerationStrategy.createWithBranchConditionCoverage()
        this.myFixture.configureByFile("/Person.java")
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val testMethod = testClass!!.findMethodsByName("setAge")[0] as PsiMethod
        val condition = PsiTreeUtil.findChildOfType(testMethod, PsiBinaryExpression::class.java)
        val expected = listOf(
            ConditionChecklistNode("Test where \"age <= 0\" is true", condition!!),
            ConditionChecklistNode("Test where \"age <= 0\" is false", condition!!),
        )
        val results = strategy.generateChecklist(condition!!)
        TestCase.assertEquals(expected, results)
    }

    @Test
    fun testCreationFromValidString() {
        val strategy = ConditionChecklistGenerationStrategy.createFromString("BRANCH")
        this.myFixture.configureByFile("/Person.java")
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val testMethod = testClass!!.findMethodsByName("setAge")[0] as PsiMethod
        val condition = PsiTreeUtil.findChildOfType(testMethod, PsiBinaryExpression::class.java)
        val expected = listOf(
            ConditionChecklistNode("Test where \"age <= 0\" is true", condition!!),
            ConditionChecklistNode("Test where \"age <= 0\" is false", condition!!),
        )
        val results = strategy.generateChecklist(condition!!)
        TestCase.assertEquals(expected, results)
    }

    @Test
    fun testCreationFromInvalidString() {
        assertFailsWith<InvalidConfigurationException> {
            ConditionChecklistGenerationStrategy.createFromString(
                "Foo"
            )
        }
    }

    @Test
    fun testBindingsToStringSimple() {
        val strategy = ConditionChecklistGenerationStrategy.createFromString("MC/DC")
        val bindings = strategy.TestCaseBindings(mapOf("A" to true, "B" to false))

        val assignments = mapOf("A" to "a", "B" to "b")

        val desc = bindings.getDescription(assignments)
        assertEquals("Test where \"a\" is true, \"b\" is false", desc)
    }

    @Test
    fun testBindingsToStringSingle() {
        val strategy = ConditionChecklistGenerationStrategy.createFromString("MC/DC")
        val bindings = strategy.TestCaseBindings(mapOf("A" to true))

        val assignments = mapOf("A" to "a")

        val desc = bindings.getDescription(assignments)
        assertEquals("Test where \"a\" is true", desc)
    }
}
