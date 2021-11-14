package com.testknight.checklistGenerationStrategies.leafStrategies.branchingStatements

import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiConditionalExpression
import com.intellij.psi.util.PsiTreeUtil
import com.testknight.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testknight.extensions.TestKnightTestCase
import com.testknight.models.testingChecklist.leafNodes.ConditionChecklistNode
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase
import org.junit.Test

internal class ConditionalExpressionChecklistGenerationStrategyTest : TestKnightTestCase() {

    private val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
    private val generationStrategy = ConditionalExpressionChecklistGenerationStrategy.create(conditionGenerationStrategy)

    @Test
    fun testLiteralCondition() {
        getBasicTestInfo("/BrokenTernary.java")
        val method = getMethodByName("conditionalExpressionWithLiteral")
        val conditionalExpression = PsiTreeUtil.findChildOfType(method, PsiConditionalExpression::class.java)

        val expected = emptyList<ConditionChecklistNode>()
        val actual = generationStrategy.generateChecklist(conditionalExpression!!)

        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testTernaryOperatorGeneration() {
        getBasicTestInfo("/Person.java")
        val method = getMethodByName("setAgeConditional")

        val conditional = PsiTreeUtil.findChildOfType(method, PsiConditionalExpression::class.java)
        val condition = PsiTreeUtil.getChildOfType(conditional, PsiBinaryExpression::class.java)

        every { conditionGenerationStrategy.generateChecklist(condition!!) } returns emptyList()
        generationStrategy.generateChecklist(conditional!!)
        verify { conditionGenerationStrategy.generateChecklist(condition!!) }
    }

    @Test
    fun testMissingConditionReturnsEmptyChecklist() {
        getBasicTestInfo("/BrokenTernary.java")
        val method = getMethodByName("incompleteConditionalExpression")

        val conditionalExpression = PsiTreeUtil.findChildOfType(method, PsiConditionalExpression::class.java)

        val expected = emptyList<ConditionChecklistNode>()
        val actual = generationStrategy.generateChecklist(conditionalExpression!!)

        TestCase.assertEquals(expected, actual)
    }
}
