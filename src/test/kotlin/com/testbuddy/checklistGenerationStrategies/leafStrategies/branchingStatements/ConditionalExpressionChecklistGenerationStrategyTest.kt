package com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements

import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiConditionalExpression
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.models.TestingChecklistLeafNode
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase
import org.junit.Test

internal class ConditionalExpressionChecklistGenerationStrategyTest : TestBuddyTestCase() {

    @Test
    fun testMissingConditionReturnsEmptyChecklist() {
        val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
        val generationStrategy = ConditionalExpressionChecklistGenerationStrategy.create(conditionGenerationStrategy)

        this.myFixture.configureByFile("/BrokenTernary.java")
        val method = getMethod("incompleteConditionalExpression")
        val conditionalExpression = PsiTreeUtil.findChildOfType(method, PsiConditionalExpression::class.java)
        val expected = emptyList<TestingChecklistLeafNode>()
        val actual = generationStrategy.generateChecklist(conditionalExpression!!)
        TestCase.assertEquals(expected, actual)
    }

    private fun getMethod(methodName: String): PsiMethod {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        return testClass!!.findMethodsByName(methodName)[0] as PsiMethod
    }

    @Test
    fun testLiteralCondition() {
        val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
        val generationStrategy = ConditionalExpressionChecklistGenerationStrategy.create(conditionGenerationStrategy)

        this.myFixture.configureByFile("/BrokenTernary.java")
        val method = getMethod("conditionalExpressionWithLiteral")
        val conditionalExpression = PsiTreeUtil.findChildOfType(method, PsiConditionalExpression::class.java)
        val expected = emptyList<TestingChecklistLeafNode>()
        val actual = generationStrategy.generateChecklist(conditionalExpression!!)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testTernaryOperatorGeneration() {
        val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
        val generationStrategy = ConditionalExpressionChecklistGenerationStrategy.create(conditionGenerationStrategy)
        this.myFixture.configureByFile("/Person.java")
        val method = getMethod("setAgeConditional")
        val conditional = PsiTreeUtil.findChildOfType(method, PsiConditionalExpression::class.java)
        val condition = PsiTreeUtil.getChildOfType(conditional, PsiBinaryExpression::class.java)
        every { conditionGenerationStrategy.generateChecklist(condition!!) } returns emptyList()
        generationStrategy.generateChecklist(conditional!!)
        verify { conditionGenerationStrategy.generateChecklist(condition!!) }
    }
}
