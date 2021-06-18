package com.testknight.checklistGenerationStrategies.leafStrategies.loopStatements

import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiDoWhileStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testknight.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testknight.extensions.TestKnightTestCase
import com.testknight.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Test

internal class DoWhileStatementChecklistGenerationStrategyTest : TestKnightTestCase() {

    private val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
    private val generationStrategy = DoWhileStatementChecklistGenerationStrategy.create(conditionGenerationStrategy)

    @Test
    fun testDoWhileChecklistReturnsOnlyOneItem() {
        getBasicTestInfo("/SimpleArray.java")

        val method = getMethodByName("incrementByOneDoWhile")
        val doWhileStatement = PsiTreeUtil.findChildOfType(method, PsiDoWhileStatement::class.java)
        val condition = PsiTreeUtil.getChildOfType(doWhileStatement, PsiBinaryExpression::class.java)

        every { conditionGenerationStrategy.generateChecklist(condition!!) } returns emptyList()
        TestCase.assertTrue(generationStrategy.generateChecklist(doWhileStatement!!).size == 1)
    }

    @Test
    fun testDoWhileChecklistCorrectDescription() {
        getBasicTestInfo("/SimpleArray.java")

        val method = getMethodByName("incrementByOneDoWhile")
        val doWhileStatement = PsiTreeUtil.findChildOfType(method, PsiDoWhileStatement::class.java)
        val condition = PsiTreeUtil.getChildOfType(doWhileStatement, PsiBinaryExpression::class.java)

        every { conditionGenerationStrategy.generateChecklist(condition!!) } returns emptyList()
        TestCase.assertTrue(
            generationStrategy.generateChecklist(doWhileStatement!!)
                .elementAt(0).description == "Test where do-while loop runs multiple times"
        )
    }

    @Test
    fun testMissingConditionReturnsEmptyList() {
        getBasicTestInfo("/SimpleArray.java")

        val method = getMethodByName("brokenDoWhile")
        val doWhileStatement = PsiTreeUtil.findChildOfType(method, PsiDoWhileStatement::class.java)

        val expected = emptyList<TestingChecklistLeafNode>()
        val actual = generationStrategy.generateChecklist(doWhileStatement!!)

        TestCase.assertEquals(expected, actual)
    }
}
