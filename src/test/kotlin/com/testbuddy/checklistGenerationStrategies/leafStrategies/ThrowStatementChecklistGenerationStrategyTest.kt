package com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiThrowStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.models.TestingChecklistLeafNode
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class ThrowStatementChecklistGenerationStrategyTest : TestBuddyTestCase() {

    private val generationStrategy = ThrowStatementChecklistGenerationStrategy.create()

    @Before
    public override fun setUp() {
        super.setUp()
        this.myFixture.configureByFile("/Person.java")
    }

    @Test
    fun testSimpleThrow() {
        val method = getMethod("getSpouse")
        val throwStatement = PsiTreeUtil.findChildOfType(method, PsiThrowStatement::class.java)
        val expected = listOf(TestingChecklistLeafNode("Test when NotMarriedException is thrown", throwStatement!!))
        val actual = generationStrategy.generateChecklist(throwStatement)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testInvalidNewExpressionInThrow() {
        val method = getMethod("methodWithBrokenThrows")
        val throwStatement = PsiTreeUtil.findChildrenOfType(method, PsiThrowStatement::class.java).elementAt(0)
        val expected = emptyList<TestingChecklistLeafNode>()
        val actual = generationStrategy.generateChecklist(throwStatement)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testMissingExceptionClassInThrow() {
        val method = getMethod("methodWithBrokenThrows")
        val throwStatement = PsiTreeUtil.findChildrenOfType(method, PsiThrowStatement::class.java).elementAt(1)
        val expected = emptyList<TestingChecklistLeafNode>()
        val actual = generationStrategy.generateChecklist(throwStatement)
        TestCase.assertEquals(expected, actual)
    }

    private fun getMethod(methodName: String): PsiMethod {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        return testClass!!.findMethodsByName(methodName)[0] as PsiMethod
    }
}
