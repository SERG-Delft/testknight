package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.com.testbuddy.models.TestClassData
import com.testbuddy.com.testbuddy.models.TestMethodData
import com.testbuddy.com.testbuddy.services.LoadTestsService
import junit.framework.TestCase
import org.junit.Test

internal class LoadTestsServiceTest : TestBuddyTestCase() {

    private val service = LoadTestsService()

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
    fun testLoadTestsFromTestClassWithTwoTests() {
        this.myFixture.configureByFile("/TwoTestClassesTest.java")
        val psi = this.myFixture.file
        val testClasses = PsiTreeUtil.findChildrenOfType(psi, PsiClass::class.java)
        val firstClass = testClasses.elementAt(0)
        val secondClass = testClasses.elementAt(1)
        val result = service.getTests(psi)
        val expected = if (firstClass != null && secondClass != null) listOf(
            TestMethodData("firstTest", "FirstTest", firstClass.findMethodsByName("firstTest")[0] as PsiMethod),
            TestMethodData("secondTest", "SecondTest", secondClass.findMethodsByName("secondTest")[0] as PsiMethod)
        ) else listOf<TestMethodData>()
        TestCase.assertEquals(expected, result)
    }

    @Test
    fun testLoadTestsFullAnnotations() {
        this.myFixture.configureByFile("/PointTestFullAnnotations.java")
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
    fun testLoadTestsTreeFromTestClassWithTwoTests() {
        this.myFixture.configureByFile("/TwoTestClassesTest.java")
        val psi = this.myFixture.file
        val testClasses = PsiTreeUtil.findChildrenOfType(psi, PsiClass::class.java)
        val firstClass = testClasses.elementAt(0)
        val secondClass = testClasses.elementAt(1)
        val result = service.getTestsTree(psi)

        val expected = listOf(
            TestClassData(
                "FirstTest",
                listOf(
                    TestMethodData(
                        "firstTest",
                        "FirstTest",
                        firstClass.findMethodsByName("firstTest")[0] as PsiMethod
                    )
                ),
                firstClass
            ),

            TestClassData(
                "SecondTest",
                listOf(
                    TestMethodData(
                        "secondTest",
                        "SecondTest",
                        secondClass.findMethodsByName("secondTest")[0] as PsiMethod
                    )
                ),
                secondClass
            )
        )

        TestCase.assertEquals(expected, result)
    }
}
