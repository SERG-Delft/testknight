package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.UsefulTestCase
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class TestAnalyzerServiceTest : BasePlatformTestCase() {

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testGetAssertionArgs() {

        this.myFixture.configureByFile("/PointTest.java")
        val testAnalyzerService = TestAnalyzerService()

        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        val testMethod = testClass.findMethodsByName("translateTest")[0] as PsiMethod

        val toBeChangedTxt = testAnalyzerService.getAssertionArgs(testMethod)
            .map { it.text }

        assertContainsElements(toBeChangedTxt, "p1", "p2")
    }

    @Test
    fun testGetConstructorArgs() {

        this.myFixture.configureByFile("/PointTest.java")
        val testAnalyzerService = TestAnalyzerService()

        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        val testMethod = testClass.findMethodsByName("translateTest")[0] as PsiMethod

        val toBeChangedTxt = testAnalyzerService.getConstructorArgs(testMethod)
            .map { it.text }

        assertContainsElements(toBeChangedTxt, "0", "0", "1", "2")
        UsefulTestCase.assertSize(4, toBeChangedTxt)
    }

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
