package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.models.ThrowsExceptionSideEffect
import com.testbuddy.com.testbuddy.services.MethodAnalyzerService
import org.junit.Test
import org.junit.jupiter.api.BeforeAll

class MethodAnalyzerServiceTest : BasePlatformTestCase() {

    val service = MethodAnalyzerService()

    @BeforeAll
    fun setup() {
        this.myFixture.configureByFile("/Person.java")
    }

    @Test
    fun testOnlyExceptionSideEffectsExplicitlyThrown() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            ThrowsExceptionSideEffect("CannotBeThatYoungException"),
            ThrowsExceptionSideEffect("CannotBeThatOldException")
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
            assertEquals(expected, service.getSideEffects(method))
        }
    }

//    @Test
//    fun testOnlyFieldMutationSideEffects() {}
//
//    @Test
//    fun testOnlyArgumentMutationSideEffects() {}
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
