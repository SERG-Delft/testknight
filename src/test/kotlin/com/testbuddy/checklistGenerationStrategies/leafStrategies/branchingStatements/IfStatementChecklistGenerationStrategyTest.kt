package com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements

import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.models.testingChecklist.leafNodes.ConditionChecklistNode
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase
import org.junit.Test

internal class IfStatementChecklistGenerationStrategyTest : TestBuddyTestCase() {

    @Test
    fun testNoCondition() {
        val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
        val generationStrategy = IfStatementChecklistGenerationStrategy.create(conditionGenerationStrategy)

        this.myFixture.configureByFile("/BrokenClass.java")
        val method = getMethod("incompleteCondition")
        val ifStatement = PsiTreeUtil.findChildOfType(method, PsiIfStatement::class.java)
        val expected = emptyList<ConditionChecklistNode>()
        val actual = generationStrategy.generateChecklist(ifStatement!!)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testLiteralCondition() {
        val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
        val generationStrategy = IfStatementChecklistGenerationStrategy.create(conditionGenerationStrategy)

        this.myFixture.configureByFile("/BrokenClass.java")
        val method = getMethod("conditionalWithLiteral")
        val ifStatement = PsiTreeUtil.findChildOfType(method, PsiIfStatement::class.java)
        val expected = emptyList<ConditionChecklistNode>()
        val actual = generationStrategy.generateChecklist(ifStatement!!)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testIfStatementGeneration() {
        val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
        val generationStrategy = IfStatementChecklistGenerationStrategy.create(conditionGenerationStrategy)
        this.myFixture.configureByFile("/Person.java")
        val method = getMethod("setAge")
        val conditional = PsiTreeUtil.findChildOfType(method, PsiIfStatement::class.java)
        val condition = PsiTreeUtil.getChildOfType(conditional, PsiBinaryExpression::class.java)
        every { conditionGenerationStrategy.generateChecklist(condition!!) } returns emptyList()
        generationStrategy.generateChecklist(conditional!!)
        verify { conditionGenerationStrategy.generateChecklist(condition!!) }
    }

    private fun getMethod(methodName: String): PsiMethod {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        return testClass!!.findMethodsByName(methodName)[0] as PsiMethod
    }
}
