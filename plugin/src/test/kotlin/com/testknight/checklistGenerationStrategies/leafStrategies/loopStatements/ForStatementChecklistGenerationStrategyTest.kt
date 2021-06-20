package com.testknight.checklistGenerationStrategies.leafStrategies.loopStatements

import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiForStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testknight.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testknight.extensions.TestKnightTestCase
import com.testknight.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Test

internal class ForStatementChecklistGenerationStrategyTest : TestKnightTestCase() {

    private val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
    private val generationStrategy = ForStatementChecklistGenerationStrategy.create(conditionGenerationStrategy)

    @Test
    fun testMissingConditionReturnsEmptyList() {
        getBasicTestInfo("/SimpleArray.java")

        val method = getMethodByName("brokenFor")
        val forStatement = PsiTreeUtil.findChildOfType(method, PsiForStatement::class.java)

        val expected = emptyList<TestingChecklistLeafNode>()
        val actual = generationStrategy.generateChecklist(forStatement!!)

        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testForChecklistReturnsOnlyOneItem() {
        getBasicTestInfo("/SimpleArray.java")

        val method = getMethodByName("incrementByOneFor")
        val forStatement = PsiTreeUtil.findChildOfType(method, PsiForStatement::class.java)
        val condition = PsiTreeUtil.getChildOfType(forStatement, PsiBinaryExpression::class.java)

        every { conditionGenerationStrategy.generateChecklist(condition!!) } returns emptyList()
        TestCase.assertTrue(generationStrategy.generateChecklist(forStatement!!).size == 1)
    }

    @Test
    fun testForChecklistCorrectDescription() {
        getBasicTestInfo("/SimpleArray.java")

        val method = getMethodByName("incrementByOneFor")
        val forStatement = PsiTreeUtil.findChildOfType(method, PsiForStatement::class.java)
        val condition = PsiTreeUtil.getChildOfType(forStatement, PsiBinaryExpression::class.java)

        every { conditionGenerationStrategy.generateChecklist(condition!!) } returns emptyList()
        TestCase.assertTrue(
            generationStrategy.generateChecklist(forStatement!!)
                .elementAt(0).description == "Test where for loop runs multiple times"
        )
    }
}
