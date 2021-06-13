package com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements

import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.models.testingChecklist.leafNodes.ConditionChecklistNode
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase
import org.junit.Test

internal class IfStatementChecklistGenerationStrategyTest : TestBuddyTestCase() {

    private val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
    private val generationStrategy = IfStatementChecklistGenerationStrategy.create(conditionGenerationStrategy)

    @Test
    fun testNoCondition() {
        getBasicTestInfo("/BrokenClass.java")
        val method = getMethodByName("incompleteCondition")

        val ifStatement = PsiTreeUtil.findChildOfType(method, PsiIfStatement::class.java)

        val expected = emptyList<ConditionChecklistNode>()
        val actual = generationStrategy.generateChecklist(ifStatement!!)

        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testIfStatementGeneration() {

        getBasicTestInfo("/Person.java")

        val method = getMethodByName("setAge")
        val conditional = PsiTreeUtil.findChildOfType(method, PsiIfStatement::class.java)
        val condition = PsiTreeUtil.getChildOfType(conditional, PsiBinaryExpression::class.java)

        every { conditionGenerationStrategy.generateChecklist(condition!!) } returns emptyList()
        generationStrategy.generateChecklist(conditional!!)
        verify { conditionGenerationStrategy.generateChecklist(condition!!) }
    }

    @Test
    fun testLiteralCondition() {

        getBasicTestInfo("/BrokenClass.java")

        val method = getMethodByName("conditionalWithLiteral")
        val ifStatement = PsiTreeUtil.findChildOfType(method, PsiIfStatement::class.java)

        val expected = emptyList<ConditionChecklistNode>()
        val actual = generationStrategy.generateChecklist(ifStatement!!)

        TestCase.assertEquals(expected, actual)
    }
}
