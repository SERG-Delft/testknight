package com.testbuddy.checklistGenerationStrategies.leafStrategies.loopStatements

import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiForStatement
import com.intellij.psi.PsiMethod
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

internal class ForStatementChecklistGenerationStrategyTest : TestBuddyTestCase() {

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
