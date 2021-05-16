package com.testbuddy.checklistGenerationStrategies.leafStrategies.loopStatements

import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiWhileStatement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.models.TestingChecklistLeafNode
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class WhileStatementChecklistGenerationStrategyTest : BasePlatformTestCase() {

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
        val generationStrategy = WhileStatementChecklistGenerationStrategy.create(conditionGenerationStrategy)

        this.myFixture.configureByFile("/SimpleArray.java")
        val method = getMethod("brokenWhile")
        val whileStatement = PsiTreeUtil.findChildOfType(method, PsiWhileStatement::class.java)
        val expected = emptyList<TestingChecklistLeafNode>()
        val actual = generationStrategy.generateChecklist(whileStatement!!)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testWhileChecklistReturnsOnlyOneItem() {
        val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
        val generationStrategy = WhileStatementChecklistGenerationStrategy.create(conditionGenerationStrategy)
        this.myFixture.configureByFile("/SimpleArray.java")
        val method = getMethod("incrementByOneWhile")
        val whileStatement = PsiTreeUtil.findChildOfType(method, PsiWhileStatement::class.java)
        val condition = PsiTreeUtil.getChildOfType(whileStatement, PsiBinaryExpression::class.java)
        every { conditionGenerationStrategy.generateChecklist(condition!!) } returns emptyList()
        TestCase.assertTrue(generationStrategy.generateChecklist(whileStatement!!).size == 1)
    }

    @Test
    fun testWhileChecklistCorrectDescription() {
        val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
        val generationStrategy = WhileStatementChecklistGenerationStrategy.create(conditionGenerationStrategy)
        this.myFixture.configureByFile("/SimpleArray.java")
        val method = getMethod("incrementByOneWhile")
        val whileStatement = PsiTreeUtil.findChildOfType(method, PsiWhileStatement::class.java)
        val condition = PsiTreeUtil.getChildOfType(whileStatement, PsiBinaryExpression::class.java)
        every { conditionGenerationStrategy.generateChecklist(condition!!) } returns emptyList()
        TestCase.assertTrue(
            generationStrategy.generateChecklist(whileStatement!!)
                .elementAt(0).description == "Test where while loop runs multiple times"
        )
    }
}
