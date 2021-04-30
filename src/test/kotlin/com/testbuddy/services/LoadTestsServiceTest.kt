package com.testbuddy.services

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
        val result = service.getTests(psi)
        val expected = listOf(
            TestCaseData("translateTest"),
            TestCaseData("setXTest"),
            TestCaseData("setYTest"),
            TestCaseData("parameterizedTest")
        )
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
