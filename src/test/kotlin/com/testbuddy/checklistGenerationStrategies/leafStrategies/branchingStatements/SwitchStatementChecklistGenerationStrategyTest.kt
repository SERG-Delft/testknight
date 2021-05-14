package com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiSwitchStatement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.SwitchStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class SwitchStatementChecklistGenerationStrategyTest : BasePlatformTestCase() {

    private val generationStrategy = SwitchStatementChecklistGenerationStrategy.create()

    @Before
    public override fun setUp() {
        super.setUp()
        this.myFixture.configureByFile("/Person.java")
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testSimpleSwitchStatement() {
        val method = getMethod("commentOnAge")
        val switch = PsiTreeUtil.findChildOfType(method, PsiSwitchStatement::class.java)
        val expected = listOf(
            TestingChecklistLeafNode("Test this.age is 10", switch!!),
            TestingChecklistLeafNode("Test this.age is 20", switch!!),
            TestingChecklistLeafNode("Test this.age is 30", switch!!),
            TestingChecklistLeafNode("Test this.age is 40", switch!!),
            TestingChecklistLeafNode("Test this.age is different from all the switch cases", switch!!)
        )
        val actual = generationStrategy.generateChecklist(switch!!)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testEnhancedSwitchStatement() {
        val method = getMethod("commentOnAgeEnhanched")
        val switch = PsiTreeUtil.findChildOfType(method, PsiSwitchStatement::class.java)
        val expected = listOf(
            TestingChecklistLeafNode("Test this.age is 10", switch!!),
            TestingChecklistLeafNode("Test this.age is 20", switch!!),
            TestingChecklistLeafNode("Test this.age is 30", switch!!),
            TestingChecklistLeafNode("Test this.age is 40", switch!!)
        )
        val actual = generationStrategy.generateChecklist(switch!!)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testEnhancedSwitchStatementWithRules() {
        val method = getMethod("commentOnAgeEnhanchedWithRules")
        val switch = PsiTreeUtil.findChildOfType(method, PsiSwitchStatement::class.java)
        val expected = listOf(
            TestingChecklistLeafNode("Test this.age is 10", switch!!),
            TestingChecklistLeafNode("Test this.age is 20", switch!!),
            TestingChecklistLeafNode("Test this.age is 30", switch!!),
            TestingChecklistLeafNode("Test this.age is 40", switch!!)
        )
        val actual = generationStrategy.generateChecklist(switch!!)
        TestCase.assertEquals(expected, actual)
    }

    private fun getMethod(methodName: String): PsiMethod {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        return testClass!!.findMethodsByName(methodName)[0] as PsiMethod
    }
}
