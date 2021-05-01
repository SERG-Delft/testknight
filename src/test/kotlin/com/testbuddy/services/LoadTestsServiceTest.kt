package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.models.TestCaseData
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class LoadTestsServiceTest : BasePlatformTestCase() {

    private val service = LoadTestsService()

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testLoadTestsSimple() {
        this.myFixture.configureByFile("/PointTest.java")
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val result = service.getTests(psi)
        val expected = if (testClass != null) listOf(
            TestCaseData("translateTest", "PointTest", testClass.findMethodsByName("translateTest")[0] as PsiMethod),
            TestCaseData("setXTest", "PointTest", testClass.findMethodsByName("setXTest")[0] as PsiMethod),
            TestCaseData("setYTest", "PointTest", testClass.findMethodsByName("setYTest")[0] as PsiMethod),
            TestCaseData("parameterizedTest", "PointTest", testClass.findMethodsByName("parameterizedTest")[0] as PsiMethod)
        ) else listOf<TestCaseData>()
        TestCase.assertEquals(expected, result)
    }

    @Test
    fun testLoadTestsEmptyTestFile() {
        this.myFixture.configureByFile("/EmptyTest.java")
        val result = service.getTests(this.myFixture.file)
        TestCase.assertEquals(listOf<TestCaseData>(), result)
    }

    @Test
    fun testLoadTestsEmptyFile() {
        this.myFixture.configureByFile("/EmptyFile.java")
        val result = service.getTests(this.myFixture.file)
        TestCase.assertEquals(listOf<TestCaseData>(), result)
    }
}
