package com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiSwitchLabelStatement
import com.intellij.psi.PsiSwitchLabeledRuleStatement
import com.intellij.psi.PsiSwitchStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements.SwitchStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.branchingStatements.SwitchStatementChecklistNode
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class SwitchStatementChecklistGenerationStrategyTest : TestBuddyTestCase() {

    private val generationStrategy = SwitchStatementChecklistGenerationStrategy.create()

    @Before
    public override fun setUp() {
        super.setUp()
        this.myFixture.configureByFile("/Person.java")
    }

    @Test
    fun testSimpleSwitchStatement() {
        val method = getMethod("commentOnAge")
        val switch = PsiTreeUtil.findChildOfType(method, PsiSwitchStatement::class.java)
        val switchLabels = PsiTreeUtil.findChildrenOfType(method, PsiSwitchLabelStatement::class.java)
        val expected = listOf(
            SwitchStatementChecklistNode("Test this.age is 10", switchLabels.elementAt(0)!!, "this.age", "10"),
            SwitchStatementChecklistNode("Test this.age is 20", switchLabels.elementAt(1)!!, "this.age", "20"),
            SwitchStatementChecklistNode("Test this.age is 30", switchLabels.elementAt(2)!!, "this.age", "30"),
            SwitchStatementChecklistNode("Test this.age is 40", switchLabels.elementAt(3)!!, "this.age", "40"),
            SwitchStatementChecklistNode(
                "Test this.age is different from all the switch cases",
                switchLabels.elementAt(4)!!,
                "this.age",
                null
            )
        )
        val actual = generationStrategy.generateChecklist(switch!!)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testEnhancedSwitchStatement() {
        val method = getMethod("commentOnAgeEnhanched")
        val switch = PsiTreeUtil.findChildOfType(method, PsiSwitchStatement::class.java)
        val switchLabels = PsiTreeUtil.findChildrenOfType(method, PsiSwitchLabelStatement::class.java)
        val expected = listOf(
            SwitchStatementChecklistNode("Test this.age is 10", switchLabels.elementAt(0)!!, "this.age", "10"),
            SwitchStatementChecklistNode("Test this.age is 20", switchLabels.elementAt(0)!!, "this.age", "20"),
            SwitchStatementChecklistNode("Test this.age is 30", switchLabels.elementAt(1)!!, "this.age", "30"),
            SwitchStatementChecklistNode("Test this.age is 40", switchLabels.elementAt(2)!!, "this.age", "40"),
        )
        val actual = generationStrategy.generateChecklist(switch!!)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testEnhancedSwitchStatementWithRules() {
        val method = getMethod("commentOnAgeEnhanchedWithRules")
        val switch = PsiTreeUtil.findChildOfType(method, PsiSwitchStatement::class.java)
        val switchRules = PsiTreeUtil.findChildrenOfType(method, PsiSwitchLabeledRuleStatement::class.java)

        val expected = listOf(
            SwitchStatementChecklistNode("Test this.age is 10", switchRules.elementAt(0)!!, "this.age", "10"),
            SwitchStatementChecklistNode("Test this.age is 20", switchRules.elementAt(0)!!, "this.age", "20"),
            SwitchStatementChecklistNode("Test this.age is 30", switchRules.elementAt(1)!!, "this.age", "30"),
            SwitchStatementChecklistNode("Test this.age is 40", switchRules.elementAt(2)!!, "this.age", "40"),
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
