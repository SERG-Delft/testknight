package com.testbuddy.services

import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiMethod
import com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.settings.SettingsService
import junit.framework.TestCase
import org.junit.Test

internal class GenerateTestCaseChecklistServiceTest : TestBuddyTestCase() {

    @Test
    fun testBasic() {
        val data = getBasicTestInfo("/Methods.java")
        val serv = GenerateTestCaseChecklistService()
        val method = data.psiClass!!.findMethodsByName("twoBranches")[0] as PsiMethod
        val output = serv.generateMethodChecklist(method)

        TestCase.assertEquals(4, output.children.size) // initial offset without scrolling is 0
    }

    @Test
    fun testDefault() {
        val data = getBasicTestInfo("/Methods.java")
        val serv = GenerateTestCaseChecklistService()
        val method = data.psiClass!!.findMethodsByName("switchCase")[0] as PsiMethod
        val output = serv.generateMethodChecklist(method)

        TestCase.assertEquals(9, output.children.size) // initial offset without scrolling is 0
    }

    @Test
    fun testDijkstra() {
        val data = getBasicTestInfo("/Methods.java")
        val serv = GenerateTestCaseChecklistService()
        val method = data.psiClass!!.findMethodsByName("dijkstra")[0] as PsiMethod
        val output = serv.generateMethodChecklist(method)

        TestCase.assertEquals(16, output.children.size) // initial offset without scrolling is 0
    }

    @Test
    fun testTernary() {
        val data = getBasicTestInfo("/Methods.java")
        val serv = GenerateTestCaseChecklistService()
        val method = data.psiClass!!.findMethodsByName("ternary")[0] as PsiMethod
        val output = serv.generateMethodChecklist(method)

        TestCase.assertEquals(2, output.children.size) // initial offset without scrolling is 0
    }

    @Test
    fun testGenerationFromMethod() {
        val data = getBasicTestInfo("/Methods.java")
        val serv = GenerateTestCaseChecklistService()
        val psiMethod = data.psiClass!!.findMethodsByName("ternary")[0] as PsiMethod
        val output = serv.generateClassChecklistFromMethod(psiMethod)

        TestCase.assertEquals(1, output.children.size)
    }

    @Test
    fun testSetMcDc() {
        val data = getBasicTestInfo("/Methods.java")
        val serv = ApplicationManager.getApplication().getService(GenerateTestCaseChecklistService::class.java)
        val psiMethod = data.psiClass!!.findMethodsByName("mcdc")[0] as PsiMethod
        SettingsService.instance.state.checklistSettings.coverageCriteria = "MC/DC"

        val output = serv.generateMethodChecklist(psiMethod)

        TestCase.assertEquals(5, output.children.size)
    }

    @Test
    fun testSetBranch() {
        val data = getBasicTestInfo("/Methods.java")
        val serv = ApplicationManager.getApplication().getService(GenerateTestCaseChecklistService::class.java)
        val psiMethod = data.psiClass!!.findMethodsByName("mcdc")[0] as PsiMethod
        SettingsService.instance.state.checklistSettings.coverageCriteria = "BRANCH"
        serv.rebuildStrategies()

        val output = serv.generateMethodChecklist(psiMethod)

        TestCase.assertEquals(2, output.children.size)
    }

    @Test
    fun testCustomParamSuggestions() {
        val data = getBasicTestInfo("/Methods.java")
        val serv = ApplicationManager.getApplication().getService(GenerateTestCaseChecklistService::class.java)
        val psiMethod = data.psiClass!!.findMethodsByName("customType")[0] as PsiMethod

        SettingsService.instance.state.checklistSettings.paramSuggestionMap["type"] = mutableListOf("a", "aa", "aaa")
        serv.rebuildStrategies()

        val output = serv.generateMethodChecklist(psiMethod)
        TestCase.assertEquals(3, output.children.size)
    }

    @Test
    fun testGenerationFromClass() {
        val data = getBasicTestInfo("/Methods.java")
        val serv = GenerateTestCaseChecklistService()
        val output = serv.generateClassChecklistFromClass(data.psiClass!!)

        TestCase.assertTrue(output.children.size > 1)
    }

    @Test
    fun testDisableParams() {
        val data = getBasicTestInfo("/Methods.java")
        val serv = ApplicationManager.getApplication().getService(GenerateTestCaseChecklistService::class.java)
        val psiMethod = data.psiClass!!.findMethodsByName("onlyParam")[0] as PsiMethod

        SettingsService.instance.state.checklistSettings.checklistStrategies["Parameter List"] = false
        serv.rebuildStrategies()

        val output = serv.generateMethodChecklist(psiMethod)
        TestCase.assertEquals(0, output.children.size)
    }

    @Test
    fun testChecklistGenerationDoesNotWorkOnTestMethods() {
        val data = getBasicTestInfo("/PointTest.java")
        val serv = GenerateTestCaseChecklistService()
        val psiMethod = data.psiClass!!.findMethodsByName("translateTest")[0] as PsiMethod

        val output = serv.generateClassChecklistFromMethod(psiMethod)

        TestCase.assertEquals(0, output.children.size)
    }
}
