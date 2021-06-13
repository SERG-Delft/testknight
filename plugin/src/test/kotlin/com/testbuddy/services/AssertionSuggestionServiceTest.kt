package com.testbuddy.services

import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.extensions.TestBuddyTestCase
import org.junit.Test

internal class AssertionSuggestionServiceTest : TestBuddyTestCase() {

    val service = AssertionSuggestionService()

    @Test
    fun testGetAssertionsWithSideEffects() {
        val data = getBasicTestInfo("/MixedTestAndNoTestMethods.java")

        val testMethod = data.psiClass!!.findMethodsByName("testMethod")[0] as PsiMethod
        val methodUnderTestCall = PsiTreeUtil.findChildOfType(testMethod!!, PsiMethodCallExpression::class.java)
        service.appendAssertionsAsComments(testMethod, methodUnderTestCall!!, project)
        this.myFixture.checkResultByFile(
            "/expected/AssertionSuggestionServiceTest.testGetAssertionsWithSideEffects.java"
        )
    }

    @Test
    fun testGetAssertionsReturnsNothing() {
        val data = getBasicTestInfo("/MixedTestAndNoTestMethods.java")

        val testMethod = data.psiClass!!.findMethodsByName("test")[0] as PsiMethod
        val methodUnderTestCall =
                PsiTreeUtil.findChildrenOfType(testMethod!!, PsiMethodCallExpression::class.java).elementAt(1)
        service.appendAssertionsAsComments(testMethod, methodUnderTestCall!!, project)
        this.myFixture.checkResultByFile(
                "/expected/AssertionSuggestionServiceTest.testGetAssertionsReturnsNothing.java"
        )
    }

    @Test
    fun testGetAssertionsOnReturnType() {
        val data = getBasicTestInfo("/MixedTestAndNoTestMethods.java")

        val testMethod = data.psiClass!!.findMethodsByName("test")[0] as PsiMethod
        val methodUnderTestCall = PsiTreeUtil.findChildOfType(testMethod!!, PsiMethodCallExpression::class.java)
        service.appendAssertionsAsComments(testMethod, methodUnderTestCall!!, project)
        this.myFixture.checkResultByFile(
            "/expected/AssertionSuggestionServiceTest.testGetAssertionsOnReturnType.java"
        )
    }
    
    @Test
    fun testDijkstraTest() {
        val data = getBasicTestInfo("/Dijkstra.java")

        val testMethod = data.psiClass!!.findMethodsByName("testDijkstra")[0] as PsiMethod
        val methodUnderTestCall =
                PsiTreeUtil.findChildrenOfType(testMethod!!, PsiMethodCallExpression::class.java).elementAt(0)
        service.appendAssertionsAsComments(testMethod, methodUnderTestCall!!, project)
        this.myFixture.checkResultByFile(
                "/expected/Dijkstra.testDijkstraTest.java"
        )
    }

    @Test
    fun testSetSpouseAssertions() {
        val data = getBasicTestInfo("/PersonMixed.java")

        val testMethod = data.psiClass!!.findMethodsByName("testSetSpouse")[0] as PsiMethod
        val methodUnderTestCall =
            PsiTreeUtil.findChildrenOfType(testMethod!!, PsiMethodCallExpression::class.java).elementAt(0)
        service.appendAssertionsAsComments(testMethod, methodUnderTestCall!!, project)
        this.myFixture.checkResultByFile(
            "/expected/PersonMixed.testSetSpouse.java"
        )
    }


    @Test
    fun testReferenceChangedAssertions() {
        val data = getBasicTestInfo("/PersonMixed.java")

        val testMethod = data.psiClass!!.findMethodsByName("testMarryToReferenceChanged")[0] as PsiMethod
        val methodUnderTestCall =
            PsiTreeUtil.findChildrenOfType(testMethod!!, PsiMethodCallExpression::class.java).elementAt(0)
        service.appendAssertionsAsComments(testMethod, methodUnderTestCall!!, project)
        this.myFixture.checkResultByFile(
            "/expected/PersonMixed.testReferenceChanged.java"
        )
    }

    @Test
    fun testDoubleShadowingAssertions() {
        val data = getBasicTestInfo("/PersonMixed.java")

        val testMethod = data.psiClass!!.findMethodsByName("testMarryToDoubleShadowing")[0] as PsiMethod
        val methodUnderTestCall =
            PsiTreeUtil.findChildrenOfType(testMethod!!, PsiMethodCallExpression::class.java).elementAt(0)
        service.appendAssertionsAsComments(testMethod, methodUnderTestCall!!, project)
        this.myFixture.checkResultByFile(
            "/expected/PersonMixed.testDoubleShadowing.java"
        )
    }

    @Test
    fun testParameterFieldAffectedAssertions() {
        val data = getBasicTestInfo("/PersonMixed.java")

        val testMethod = data.psiClass!!.findMethodsByName("testMarryToParameterFieldAffected")[0] as PsiMethod
        val methodUnderTestCall =
            PsiTreeUtil.findChildrenOfType(testMethod!!, PsiMethodCallExpression::class.java).elementAt(0)
        service.appendAssertionsAsComments(testMethod, methodUnderTestCall!!, project)
        this.myFixture.checkResultByFile(
            "/expected/PersonMixed.testParameterFieldAffected.java"
        )
    }
}
