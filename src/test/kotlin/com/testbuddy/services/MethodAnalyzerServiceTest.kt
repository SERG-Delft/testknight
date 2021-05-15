package com.testbuddy.services

import com.intellij.psi.PsiAssignmentExpression
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.models.ReassignsClassFieldSideEffect
import com.testbuddy.com.testbuddy.models.SideEffect
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import java.lang.AssertionError

class MethodAnalyzerServiceTest : BasePlatformTestCase() {

    private val service = MethodAnalyzerService()

    @Before
    public override fun setUp() {
        super.setUp()
        this.myFixture.configureByFile("/Person.java")
    }

    @Test
    fun testFieldMutationSideEffectsThisNotSpecified() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val assignmentExpression = PsiTreeUtil.findChildOfType(psi, PsiAssignmentExpression::class.java)
        val expected = listOf(ReassignsClassFieldSideEffect("this.name"))
        assertMethodSideEffects(testClass, expected, "setFullName")
    }

    @Test
    fun testOnlyArgumentMutationSideEffectsThisSpecified() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val assignmentExpression = PsiTreeUtil.findChildOfType(psi, PsiAssignmentExpression::class.java)
        val expected = listOf(ReassignsClassFieldSideEffect("this.name"))
        assertMethodSideEffects(testClass, expected, "setName")
    }

    @Test
    fun testNoSideEffect() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf<SideEffect>()
        assertMethodSideEffects(testClass, expected, "greet")
    }

//    @Test
//    fun testMethodCallOnParameter() {}
//
//    @Test
//    fun testMethodCallWithParameterPassed() {}
//
//    @Test
//    fun testMethodCallOnClassField() {
//    val psi = this.myFixture.file
//    val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
//    val assignmentExpression = PsiTreeUtil.findChildOfType(psi, PsiAssignmentExpression::class.java)
//    val expected = listOf(ReassignsClassFieldSideEffect("this.name"))
//    assertMethodSideEffects(testClass, expected, "methodCall")
//    }
//
//    @Test
//    fun testMethodCallWithClassFieldPassed() {}

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    /**
     * This is a helper method to assert that a method has certain side-effects.
     *
     * @param testClass the PsiClass (nullable) that contains the method analyzed.
     * @param expected the expected list of side-effects.
     * @param methodName the name of the method that we want to take the side-effect.
     */
    private fun assertMethodSideEffects(testClass: PsiClass?, expected: List<SideEffect>, methodName: String) {
        if (testClass != null) {
            val method = testClass.findMethodsByName(methodName)[0] as PsiMethod
            val actual = service.getSideEffects(method)
            TestCase.assertEquals(expected, actual)
        } else {
            throw AssertionError("testClass was null")
        }
    }
}
