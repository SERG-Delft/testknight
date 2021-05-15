package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
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

        val output = serv.methodChecklistGenerationStrategy.generateChecklist(method)
    }

    @Test
    fun testDefault() {
        myFixture.configureByFile("/Methods.java")
        val serv = GenerateTestCaseChecklistService()
        val psiClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)
        val method = psiClass!!.findMethodsByName("switchCase")[0] as PsiMethod

        val output = serv.methodChecklistGenerationStrategy.generateChecklist(method)
    }

    @Test
    fun testDijkstra() {
        myFixture.configureByFile("/Methods.java")
        val serv = GenerateTestCaseChecklistService()
        val psiClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)
        val method = psiClass!!.findMethodsByName("dijkstra")[0] as PsiMethod

        val output = serv.methodChecklistGenerationStrategy.generateChecklist(method)
    }

    @Test
    fun testMcdc() {
        myFixture.configureByFile("/Methods.java")
        val serv = GenerateTestCaseChecklistService()
        val psiClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)
        val method = psiClass!!.findMethodsByName("mcdc")[0] as PsiMethod

        val output = serv.methodChecklistGenerationStrategy.generateChecklist(method)
    }
}
