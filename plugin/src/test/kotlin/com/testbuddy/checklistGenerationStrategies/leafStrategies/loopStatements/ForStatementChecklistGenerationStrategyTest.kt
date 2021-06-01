package com.testbuddy.checklistGenerationStrategies.leafStrategies.loopStatements

import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiForStatement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class ForStatementChecklistGenerationStrategyTest : BasePlatformTestCase() {

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
        val generationStrategy = ForStatementChecklistGenerationStrategy.create(conditionGenerationStrategy)

        this.myFixture.configureByFile("/SimpleArray.java")
        val method = getMethod("brokenFor")
        val forStatement = PsiTreeUtil.findChildOfType(method, PsiForStatement::class.java)
        val expected = emptyList<TestingChecklistLeafNode>()
        val actual = generationStrategy.generateChecklist(forStatement!!)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testForChecklistReturnsOnlyOneItem() {
        val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
        val generationStrategy = ForStatementChecklistGenerationStrategy.create(conditionGenerationStrategy)
        this.myFixture.configureByFile("/SimpleArray.java")
        val method = getMethod("incrementByOneFor")
        val forStatement = PsiTreeUtil.findChildOfType(method, PsiForStatement::class.java)
        val condition = PsiTreeUtil.getChildOfType(forStatement, PsiBinaryExpression::class.java)
        every { conditionGenerationStrategy.generateChecklist(condition!!) } returns emptyList()
        TestCase.assertTrue(generationStrategy.generateChecklist(forStatement!!).size == 1)
    }

    @Test
    fun testForChecklistCorrectDescription() {
        val conditionGenerationStrategy = mockk<ConditionChecklistGenerationStrategy>()
        val generationStrategy = ForStatementChecklistGenerationStrategy.create(conditionGenerationStrategy)
        this.myFixture.configureByFile("/SimpleArray.java")
        val method = getMethod("incrementByOneFor")
        val forStatement = PsiTreeUtil.findChildOfType(method, PsiForStatement::class.java)
        val condition = PsiTreeUtil.getChildOfType(forStatement, PsiBinaryExpression::class.java)
        every { conditionGenerationStrategy.generateChecklist(condition!!) } returns emptyList()
        TestCase.assertTrue(
            generationStrategy.generateChecklist(forStatement!!)
                .elementAt(0).description == "Test where for loop runs multiple times"
        )
    }
}
