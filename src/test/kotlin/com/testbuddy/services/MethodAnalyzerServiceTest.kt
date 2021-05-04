package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.models.MutatesClassFieldSideEffect
import com.testbuddy.com.testbuddy.models.ThrowsExceptionSideEffect
import com.testbuddy.com.testbuddy.services.MethodAnalyzerService
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class MethodAnalyzerServiceTest : BasePlatformTestCase() {

    val service = MethodAnalyzerService()

    @Before
    public override fun setUp() {
        super.setUp()
        this.myFixture.configureByFile("/Person.java")
    }

    @Test
    fun testExceptionSideEffectsExplicitlyThrown() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            ThrowsExceptionSideEffect("CannotBeThatYoungException"),
            ThrowsExceptionSideEffect("CannotBeThatOldException"),
            MutatesClassFieldSideEffect("this.age")
        )
        if (testClass != null) {
            val method = testClass.findMethodsByName("setAge")[0] as PsiMethod
            assertEquals(expected, service.getSideEffects(method))
        }
    }

    @Test
    fun testOnlyExceptionSideEffectsOnlyDeclaration() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(ThrowsExceptionSideEffect("IOException"))
        if (testClass != null) {
            val method = testClass.findMethodsByName("save")[0] as PsiMethod
            TestCase.assertEquals(expected, service.getSideEffects(method))
        }
    }

//    @Test
//    fun testOnlyFieldMutationSideEffects() {}

    @Test
    fun testFieldMutationSideEffectsThisNotSpecified() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(MutatesClassFieldSideEffect("this.name"))
        if (testClass != null) {
            val method = testClass.findMethodsByName("setFullName")[0] as PsiMethod
            TestCase.assertEquals(expected, service.getSideEffects(method))
        } else {
            TestCase.assertTrue(false) // if we reached here the test should fail
        }
    }

    @Test
    fun testOnlyArgumentMutationSideEffectsThisSpecified() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(MutatesClassFieldSideEffect("this.name"))
        if (testClass != null) {
            val method = testClass.findMethodsByName("setName")[0] as PsiMethod
            TestCase.assertEquals(expected, service.getSideEffects(method))
        } else {
            TestCase.assertTrue(false) // if we reached here the test should fail
        }
    }
//
//    @Test
//    fun testAllPossibleSideEffects() {}
//
//    @Test
//    fun testNoSideEffect() {}

    public override fun getTestDataPath(): String {
        return "testdata"
    }
}
