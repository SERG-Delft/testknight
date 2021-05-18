package com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.exceptions.InvalidConfigurationException
import com.testbuddy.models.TestingChecklistLeafNode
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

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
        val strategy = ConditionChecklistGenerationStrategy.createWithMcDcConditionCoverage()

        val testCases = strategy.mcdc(listOf(), "true")

        assertEmpty(testCases)
    }

    @Test
    fun testMcdc1Prop() {
        val strategy = ConditionChecklistGenerationStrategy.createWithMcDcConditionCoverage()

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
        val strategy = ConditionChecklistGenerationStrategy.createWithMcDcConditionCoverage()

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
        val strategy = ConditionChecklistGenerationStrategy.createWithMcDcConditionCoverage()

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
        val strategy = ConditionChecklistGenerationStrategy.createWithMcDcConditionCoverage()

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

    @Test
    fun testBranchCoverage() {
        val strategy = ConditionChecklistGenerationStrategy.createWithBranchConditionCoverage()
        this.myFixture.configureByFile("/Person.java")
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val testMethod = testClass!!.findMethodsByName("setAge")[0] as PsiMethod
        val condition = PsiTreeUtil.findChildOfType(testMethod, PsiBinaryExpression::class.java)
        val expected = listOf(
            TestingChecklistLeafNode("Test where in the condition \"age <= 0\", \"age <= 0\" is true", condition!!),
            TestingChecklistLeafNode("Test where in the condition \"age <= 0\", \"age <= 0\" is false", condition!!),
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
            TestingChecklistLeafNode("Test where in the condition \"age <= 0\", \"age <= 0\" is true", condition!!),
            TestingChecklistLeafNode("Test where in the condition \"age <= 0\", \"age <= 0\" is false", condition!!),
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
}
