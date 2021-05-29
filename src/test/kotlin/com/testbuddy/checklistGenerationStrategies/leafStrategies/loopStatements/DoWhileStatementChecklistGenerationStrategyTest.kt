package com.testbuddy.checklistGenerationStrategies.leafStrategies.loopStatements

import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDoWhileStatement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class DoWhileStatementChecklistGenerationStrategyTest : BasePlatformTestCase() {

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    private fun getMethod(methodName: String): PsiMethod {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        return testClass!!.findMethodsByName(methodName)[0] as PsiMethod
    }

    @Test
    fun testMissingConditionReturnsEmptyList() {
        val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
        val generationStrategy = DoWhileStatementChecklistGenerationStrategy.create(conditionGenerationStrategy)

        this.myFixture.configureByFile("/SimpleArray.java")
        val method = getMethod("brokenDoWhile")
        val doWhileStatement = PsiTreeUtil.findChildOfType(method, PsiDoWhileStatement::class.java)
        val expected = emptyList<TestingChecklistLeafNode>()
        val actual = generationStrategy.generateChecklist(doWhileStatement!!)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testDoWhileChecklistReturnsOnlyOneItem() {
        val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
        val generationStrategy = DoWhileStatementChecklistGenerationStrategy.create(conditionGenerationStrategy)
        this.myFixture.configureByFile("/SimpleArray.java")
        val method = getMethod("incrementByOneDoWhile")
        val doWhileStatement = PsiTreeUtil.findChildOfType(method, PsiDoWhileStatement::class.java)
        val condition = PsiTreeUtil.getChildOfType(doWhileStatement, PsiBinaryExpression::class.java)
        every { conditionGenerationStrategy.generateChecklist(condition!!) } returns emptyList()
        TestCase.assertTrue(generationStrategy.generateChecklist(doWhileStatement!!).size == 1)
    }

    @Test
    fun testDoWhileChecklistCorrectDescription() {
        val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
        val generationStrategy = DoWhileStatementChecklistGenerationStrategy.create(conditionGenerationStrategy)
        this.myFixture.configureByFile("/SimpleArray.java")
        val method = getMethod("incrementByOneDoWhile")
        val doWhileStatement = PsiTreeUtil.findChildOfType(method, PsiDoWhileStatement::class.java)
        val condition = PsiTreeUtil.getChildOfType(doWhileStatement, PsiBinaryExpression::class.java)
        every { conditionGenerationStrategy.generateChecklist(condition!!) } returns emptyList()
        TestCase.assertTrue(
            generationStrategy.generateChecklist(doWhileStatement!!)
                .elementAt(0).description == "Test where do-while loop runs multiple times"
        )
    }
}
