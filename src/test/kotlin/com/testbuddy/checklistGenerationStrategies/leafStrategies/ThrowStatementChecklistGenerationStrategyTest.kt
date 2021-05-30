package com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiThrowStatement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.models.testingChecklist.leafNodes.ThrowStatementChecklistNode
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class ThrowStatementChecklistGenerationStrategyTest : BasePlatformTestCase() {

    private val generationStrategy = ThrowStatementChecklistGenerationStrategy.create()

    @Before
    public override fun setUp() {
        super.setUp()
        this.myFixture.configureByFile("/Person.java")
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testSimpleThrow() {
        val method = getMethod("getSpouse")
        val throwStatement = PsiTreeUtil.findChildOfType(method, PsiThrowStatement::class.java)
        val expected = listOf(ThrowStatementChecklistNode("Test when NotMarriedException is thrown", throwStatement!!, "NotMarriedException"))
        val actual = generationStrategy.generateChecklist(throwStatement)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testInvalidNewExpressionInThrow() {
        val method = getMethod("methodWithBrokenThrows")
        val throwStatement = PsiTreeUtil.findChildrenOfType(method, PsiThrowStatement::class.java).elementAt(0)
        val expected = emptyList<ThrowStatementChecklistNode>()
        val actual = generationStrategy.generateChecklist(throwStatement)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testMissingExceptionClassInThrow() {
        val method = getMethod("methodWithBrokenThrows")
        val throwStatement = PsiTreeUtil.findChildrenOfType(method, PsiThrowStatement::class.java).elementAt(1)
        val expected = emptyList<ThrowStatementChecklistNode>()
        val actual = generationStrategy.generateChecklist(throwStatement)
        TestCase.assertEquals(expected, actual)
    }

    private fun getMethod(methodName: String): PsiMethod {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        return testClass!!.findMethodsByName(methodName)[0] as PsiMethod
    }
}
