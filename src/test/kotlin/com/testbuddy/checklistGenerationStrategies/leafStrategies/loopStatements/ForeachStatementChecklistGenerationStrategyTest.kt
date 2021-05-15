package com.testbuddy.checklistGenerationStrategies.leafStrategies.loopStatements

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class ForeachStatementChecklistGenerationStrategyTest : BasePlatformTestCase() {

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
    fun testMissingIteratedValueReturnsEmptyList() {
        val generationStrategy = ForeachStatementChecklistGenerationStrategy.create()

        this.myFixture.configureByFile("/SimpleArray.java")
        val method = getMethod("brokenForEach")
        val foreachStatement = PsiTreeUtil.findChildOfType(method, PsiForeachStatement::class.java)
        val expected = emptyList<TestingChecklistLeafNode>()
        val actual = generationStrategy.generateChecklist(foreachStatement!!)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testForeachChecklistGenerationCorrect() {
        val generationStrategy = ForeachStatementChecklistGenerationStrategy.create()

        this.myFixture.configureByFile("/SimpleArray.java")
        val method = getMethod("incrementByOneForEach")
        val foreachStatement = PsiTreeUtil.findChildOfType(method, PsiForeachStatement::class.java)
        val expected = listOf(
            TestingChecklistLeafNode(description = "Test where getArrayOfInts() is empty", foreachStatement as PsiElement),
            TestingChecklistLeafNode(description = "Test where getArrayOfInts() is null", foreachStatement as PsiElement),
            TestingChecklistLeafNode(description = "Test where foreach loop runs multiple times", foreachStatement as PsiElement)
        )
        val actual = generationStrategy.generateChecklist(foreachStatement!!)
        TestCase.assertEquals(expected, actual)
    }
}
