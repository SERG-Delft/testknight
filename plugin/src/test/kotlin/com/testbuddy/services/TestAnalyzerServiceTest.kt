package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.extensions.TestBuddyTestCase
import junit.framework.TestCase
import org.junit.Test

internal class TestAnalyzerServiceTest : TestBuddyTestCase() {

    @Test
    fun testIsTestClassTrue() {
        this.myFixture.configureByFile("/PointTest.java")
        val testAnalyzerService = TestAnalyzerService()
        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        TestCase.assertTrue(testAnalyzerService.isTestClass(testClass))
    }

    @Test
    fun testIsTestClassFalse() {
        this.myFixture.configureByFile("/Person.java")
        val testAnalyzerService = TestAnalyzerService()
        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        TestCase.assertFalse(testAnalyzerService.isTestClass(testClass))
    }

    @Test
    fun testIsTestClassEmptyClass() {
        this.myFixture.configureByFile("/EmptyClass.java")
        val testAnalyzerService = TestAnalyzerService()
        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        TestCase.assertFalse(testAnalyzerService.isTestClass(testClass))
    }
}
