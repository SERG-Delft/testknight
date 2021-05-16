package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Before
import org.junit.Test

internal class AssertionSuggestionServiceTest : BasePlatformTestCase() {
    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testGetAssertionsOnReturnType() {
        this.myFixture.configureByFile("/MixedTestAndNoTestMethods.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project

        val service = AssertionSuggestionService()
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val testMethod = testClass!!.findMethodsByName("test")[0] as PsiMethod
        val methodUnderTestCall = PsiTreeUtil.findChildOfType(testMethod!!, PsiMethodCallExpression::class.java)
        service.appendAssertionsAsComments(testMethod, methodUnderTestCall!!, project)
        this.myFixture.checkResultByFile(
            "/expected/AssertionSuggestionServiceTest.testGetAssertionsOnReturnType.java"
        )
    }

    @Test
    fun testGetAssertionsReturnsNothing() {
        this.myFixture.configureByFile("/MixedTestAndNoTestMethods.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project

        val service = AssertionSuggestionService()
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val testMethod = testClass!!.findMethodsByName("test")[0] as PsiMethod
        val methodUnderTestCall =
            PsiTreeUtil.findChildrenOfType(testMethod!!, PsiMethodCallExpression::class.java).elementAt(1)
        service.appendAssertionsAsComments(testMethod, methodUnderTestCall!!, project)
        this.myFixture.checkResultByFile(
            "/expected/AssertionSuggestionServiceTest.testGetAssertionsReturnsNothing.java"
        )
    }
}
