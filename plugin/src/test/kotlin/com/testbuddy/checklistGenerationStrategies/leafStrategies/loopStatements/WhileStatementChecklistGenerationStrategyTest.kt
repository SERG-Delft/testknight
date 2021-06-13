package com.testbuddy.checklistGenerationStrategies.leafStrategies.loopStatements

import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiWhileStatement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class WhileStatementChecklistGenerationStrategyTest : TestBuddyTestCase() {

    private val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
    private val generationStrategy = WhileStatementChecklistGenerationStrategy.create(conditionGenerationStrategy)

    @Test
    fun testMissingConditionReturnsEmptyList() {
        getBasicTestInfo("/SimpleArray.java")

        val method = getMethodByName("brokenWhile")
        val whileStatement = PsiTreeUtil.findChildOfType(method, PsiWhileStatement::class.java)

        val expected = emptyList<TestingChecklistLeafNode>()
        val actual = generationStrategy.generateChecklist(whileStatement!!)

        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testWhileChecklistReturnsOnlyOneItem() {
        getBasicTestInfo("/SimpleArray.java")

        val method = getMethodByName("incrementByOneWhile")
        val whileStatement = PsiTreeUtil.findChildOfType(method, PsiWhileStatement::class.java)
        val condition = PsiTreeUtil.getChildOfType(whileStatement, PsiBinaryExpression::class.java)

        every { conditionGenerationStrategy.generateChecklist(condition!!) } returns emptyList()
        TestCase.assertTrue(generationStrategy.generateChecklist(whileStatement!!).size == 1)
    }

    @Test
    fun testWhileChecklistCorrectDescription() {
        getBasicTestInfo("/SimpleArray.java")

        val method = getMethodByName("incrementByOneWhile")
        val whileStatement = PsiTreeUtil.findChildOfType(method, PsiWhileStatement::class.java)
        val condition = PsiTreeUtil.getChildOfType(whileStatement, PsiBinaryExpression::class.java)

        every { conditionGenerationStrategy.generateChecklist(condition!!) } returns emptyList()
        TestCase.assertTrue(
            generationStrategy.generateChecklist(whileStatement!!)
                .elementAt(0).description == "Test where while loop runs multiple times"
        )
    }
}
