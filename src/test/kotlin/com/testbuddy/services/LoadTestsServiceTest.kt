package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.models.TestMethodData
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
            TestMethodData("translateTest", "PointTest", testClass.findMethodsByName("translateTest")[0] as PsiMethod),
            TestMethodData("setXTest", "PointTest", testClass.findMethodsByName("setXTest")[0] as PsiMethod),
            TestMethodData("setYTest", "PointTest", testClass.findMethodsByName("setYTest")[0] as PsiMethod),
            TestMethodData("parameterizedTest", "PointTest", testClass.findMethodsByName("parameterizedTest")[0] as PsiMethod)
        ) else listOf<TestMethodData>()
        TestCase.assertEquals(expected, result)
    }

    @Test
    fun testLoadTestsEmptyTestFile() {
        this.myFixture.configureByFile("/EmptyTest.java")
        val result = service.getTests(this.myFixture.file)
        TestCase.assertEquals(listOf<TestMethodData>(), result)
    }

    @Test
    fun testLoadTestsEmptyFile() {
        this.myFixture.configureByFile("/EmptyFile.java")
        val result = service.getTests(this.myFixture.file)
        TestCase.assertEquals(listOf<TestMethodData>(), result)
    }
}
