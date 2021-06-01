package com.testbuddy.services

import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.settings.SettingsService
import junit.framework.TestCase
import org.junit.Test

internal class GenerateTestCaseChecklistServiceTest : TestBuddyTestCase() {

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

    @Test
    fun testSetMcDc() {
        myFixture.configureByFile("/Methods.java")
        val serv = ApplicationManager.getApplication().getService(GenerateTestCaseChecklistService::class.java)
        val psiClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("mcdc")[0] as PsiMethod

        SettingsService.instance.state.checklistSettings.coverageCriteria = "MC/DC"

        val output = serv.generateMethodChecklist(psiMethod)

        TestCase.assertEquals(5, output.children.size)
    }

    @Test
    fun testSetBranch() {
        myFixture.configureByFile("/Methods.java")
        val serv = ApplicationManager.getApplication().getService(GenerateTestCaseChecklistService::class.java)
        val psiClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("mcdc")[0] as PsiMethod

        SettingsService.instance.state.checklistSettings.coverageCriteria = "BRANCH"
        serv.rebuildStrategies()

        val output = serv.generateMethodChecklist(psiMethod)
        TestCase.assertEquals(2, output.children.size)
    }

    @Test
    fun testDisableParams() {
        myFixture.configureByFile("/Methods.java")
        val serv = ApplicationManager.getApplication().getService(GenerateTestCaseChecklistService::class.java)
        val psiClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("onlyParam")[0] as PsiMethod

        SettingsService.instance.state.checklistSettings.checklistStrategies["Parameter List"] = false
        serv.rebuildStrategies()

        val output = serv.generateMethodChecklist(psiMethod)
        TestCase.assertEquals(0, output.children.size)
    }

    @Test
    fun testCustomParamSuggestions() {
        myFixture.configureByFile("/Methods.java")
        val serv = ApplicationManager.getApplication().getService(GenerateTestCaseChecklistService::class.java)
        val psiClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("customType")[0] as PsiMethod

        SettingsService.instance.state.checklistSettings.typeCaseMap["type"] = listOf("a", "aa", "aaa")
        serv.rebuildStrategies()

        val output = serv.generateMethodChecklist(psiMethod)
        TestCase.assertEquals(3, output.children.size)
    }
}
