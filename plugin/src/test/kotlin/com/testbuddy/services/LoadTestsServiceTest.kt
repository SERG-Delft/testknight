package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.models.TestClassData
import com.testbuddy.models.TestMethodData
import junit.framework.TestCase
import org.junit.Test

internal class LoadTestsServiceTest : TestBuddyTestCase() {

    private val service = LoadTestsService()

    @Test
    fun testLoadTestsSimple() {
        val data = getBasicTestInfo("/PointTest.java")

        val result = service.getTests(data.psiFile)
        val expected = if (data.psiClass != null) listOf(
                TestMethodData("translateTest", "PointTest", data.psiClass!!.findMethodsByName("translateTest")[0] as PsiMethod),
                TestMethodData("setXTest", "PointTest", data.psiClass!!.findMethodsByName("setXTest")[0] as PsiMethod),
                TestMethodData("setYTest", "PointTest", data.psiClass!!.findMethodsByName("setYTest")[0] as PsiMethod),
                TestMethodData("parameterizedTest", "PointTest", data.psiClass!!.findMethodsByName("parameterizedTest")[0] as PsiMethod)
        ) else listOf<TestMethodData>()

        TestCase.assertEquals(expected, result)
    }

    @Test
    fun testLoadTestsFullAnnotations() {
        val data = getBasicTestInfo("/PointTestFullAnnotations.java")
        val result = service.getTests(data.psiFile)
        val expected = if (data.psiClass != null) listOf(
                TestMethodData("translateTest", "PointTest", data.psiClass!!.findMethodsByName("translateTest")[0] as PsiMethod),
                TestMethodData("setXTest", "PointTest", data.psiClass!!.findMethodsByName("setXTest")[0] as PsiMethod),
                TestMethodData("setYTest", "PointTest", data.psiClass!!.findMethodsByName("setYTest")[0] as PsiMethod),
                TestMethodData("parameterizedTest", "PointTest", data.psiClass!!.findMethodsByName("parameterizedTest")[0] as PsiMethod)
        ) else listOf<TestMethodData>()
        TestCase.assertEquals(expected, result)
    }

    @Test
    fun testLoadTestsFromTestClassWithTwoTests() {
        val data = getBasicTestInfo("/TwoTestClassesTest.java")

        val firstClass = data.testClasses.elementAt(0)
        val secondClass = data.testClasses.elementAt(1)
        val result = service.getTests(data.psiFile)
        val expected = if (firstClass != null && secondClass != null) listOf(
                TestMethodData("firstTest", "FirstTest", firstClass.findMethodsByName("firstTest")[0] as PsiMethod),
                TestMethodData("secondTest", "SecondTest", secondClass.findMethodsByName("secondTest")[0] as PsiMethod)
        ) else listOf<TestMethodData>()
        TestCase.assertEquals(expected, result)
    }

    @Test
    fun testLoadTestsTreeFromTestClassWithTwoTests() {
        val data = getBasicTestInfo("/TwoTestClassesTest.java")

        val firstClass = data.testClasses.elementAt(0)
        val secondClass = data.testClasses.elementAt(1)
        val result = service.getTestsTree(data.psiFile)

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
