package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class GenerateTestCaseChecklistServiceTest : BasePlatformTestCase() {

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testBasic() {
        myFixture.configureByFile("/Methods.java")
        val serv = GenerateTestCaseChecklistService()
        val psiClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)
        val method = psiClass!!.findMethodsByName("twoBranches")[0] as PsiMethod

        val output = serv.generateMethodChecklist(method)
        TestCase.assertEquals(4, output.children.size) // initial offset without scrolling is 0
    }

    @Test
    fun testDefault() {
        myFixture.configureByFile("/Methods.java")
        val serv = GenerateTestCaseChecklistService()
        val psiClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)
        val method = psiClass!!.findMethodsByName("switchCase")[0] as PsiMethod

        val output = serv.generateMethodChecklist(method)
        TestCase.assertEquals(9, output.children.size) // initial offset without scrolling is 0
    }

    @Test
    fun testDijkstra() {
        myFixture.configureByFile("/Methods.java")
        val serv = GenerateTestCaseChecklistService()
        val psiClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)
        val method = psiClass!!.findMethodsByName("dijkstra")[0] as PsiMethod

        val output = serv.generateMethodChecklist(method)
        TestCase.assertEquals(16, output.children.size) // initial offset without scrolling is 0
    }

    @Test
    fun testTernary() {
        myFixture.configureByFile("/Methods.java")
        val serv = GenerateTestCaseChecklistService()
        val psiClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)
        val method = psiClass!!.findMethodsByName("ternary")[0] as PsiMethod

        val output = serv.generateMethodChecklist(method)
        TestCase.assertEquals(2, output.children.size) // initial offset without scrolling is 0
    }

    @Test
    fun testGenerationFromClass() {
        myFixture.configureByFile("/Methods.java")
        val serv = GenerateTestCaseChecklistService()
        val psiClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)

        val output = serv.generateClassChecklistFromClass(psiClass!!)

        TestCase.assertTrue(output.children.size > 1)
    }

    @Test
    fun testGenerationFromMethod() {
        myFixture.configureByFile("/Methods.java")
        val serv = GenerateTestCaseChecklistService()
        val psiClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("ternary")[0] as PsiMethod

        val output = serv.generateClassChecklistFromMethod(psiMethod)

        TestCase.assertEquals(1, output.children.size)
    }

    @Test
    fun testChecklistGenerationDoesNotWorkOnTestMethods() {
        myFixture.configureByFile("/PointTest.java")
        val serv = GenerateTestCaseChecklistService()
        val psiClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("translateTest")[0] as PsiMethod

        val output = serv.generateClassChecklistFromMethod(psiMethod)

        TestCase.assertEquals(0, output.children.size)
    }
}
