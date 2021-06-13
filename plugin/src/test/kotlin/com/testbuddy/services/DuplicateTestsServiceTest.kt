package com.testbuddy.services

import com.intellij.psi.PsiMethod
import com.intellij.refactoring.suggested.startOffset
import com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.settings.SettingsService
import junit.framework.TestCase
import org.junit.jupiter.api.Test

class DuplicateTestsServiceTest : TestBuddyTestCase() {

    /**
     * This test class is responsible for verifying the test duplication and highlight-conflict algorithm work
     * correctly. To test the individual highlight resolution strategies see their own test classes
     */

    @Test
    fun testDuplicateMethodUnderCaret() {
        val data = getBasicTestInfo("/PointTest.java")

        val service = DuplicateTestsService(project)
        if (data.psiClass != null) {
            val methodToBeDuplicated = data.psiClass!!.findMethodsByName("translateTest")[0] as PsiMethod
            data.editor.caretModel.primaryCaret.moveToOffset(methodToBeDuplicated.startOffset)

            service.duplicateMethodUnderCaret(data.psiFile, data.editor)

            this.myFixture.checkResultByFile(
                "/expected/DuplicateTestsServiceTest.testDuplicateMethodUnderCaret.java"
            )
        }
    }

    @Test
    fun testDuplicateMethod() {
        val data = getBasicTestInfo("/PointTest.java")

        val service = DuplicateTestsService(project)
        if (data.psiClass != null) {
            val methodToBeDuplicated = data.psiClass!!.findMethodsByName("translateTest")[0] as PsiMethod

            service.duplicateMethod(methodToBeDuplicated, data.editor)

            this.myFixture.checkResultByFile(
                "/expected/DuplicateTestsServiceTest.testDuplicateMethod.java"
            )
        }
    }

    @Test
    fun testDuplicates() {
        val data = getBasicTestInfo("/TestDuplication.java")

        val service = DuplicateTestsService(project)
        val methodToBeDuplicated = data.psiClass!!.findMethodsByName("duplicate")[0] as PsiMethod
        val strategySettings = SettingsService.instance.state.testListSettings.highlightStrategies
        for (key in strategySettings.keys) {
            strategySettings[key] = true
        }

        val highlights = service.getHighlights(methodToBeDuplicated).map { it.text }
        val expectedHl = listOf("0", "1")

        TestCase.assertEquals(expectedHl, highlights)
    }

    @Test
    fun testContaining() {
        val data = getBasicTestInfo("/TestDuplication.java")

        val service = DuplicateTestsService(project)
        val methodToBeDuplicated = data.psiClass!!.findMethodsByName("containing")[0] as PsiMethod
        val strategySettings = SettingsService.instance.state.testListSettings.highlightStrategies
        for (key in strategySettings.keys) {
            strategySettings[key] = true
        }

        val highlights = service.getHighlights(methodToBeDuplicated).map { it.text }
        val expectedHl = listOf("0", "1", "2")

        TestCase.assertEquals(expectedHl, highlights)
    }

    @Test
    fun testNestedContaining() {
        val data = getBasicTestInfo("/TestDuplication.java")

        val service = DuplicateTestsService(project)
        val methodToBeDuplicated = data.psiClass!!.findMethodsByName("nestedContains")[0] as PsiMethod
        val strategySettings = SettingsService.instance.state.testListSettings.highlightStrategies
        for (key in strategySettings.keys) {
            strategySettings[key] = true
        }

        val highlights = service.getHighlights(methodToBeDuplicated).map { it.text }
        val expectedHl = listOf("0", "1", "2", "3", "dar()")

        TestCase.assertEquals(expectedHl, highlights)
    }

    @Test
    fun testHighlightingInQuotes() {
        val data = getBasicTestInfo("/TestDuplication.java")

        val service = DuplicateTestsService(project)
        val methodToBeDuplicated = data.psiClass!!.findMethodsByName("strAndChar")[0] as PsiMethod

        val strategySettings = SettingsService.instance.state.testListSettings.highlightStrategies
        for (key in strategySettings.keys) {
            strategySettings[key] = false
        }
        strategySettings["Highlight literals"] = true
        strategySettings["Highlight string inside quotes"] = true

        val highlights = service.getHighlights(methodToBeDuplicated).map { it.text }
        val expectedHl = listOf("string", "c")

        TestCase.assertEquals(expectedHl, highlights)
    }

    @Test
    fun testHighlightingOutOfQuotes() {
        val data = getBasicTestInfo("/TestDuplication.java")

        val service = DuplicateTestsService(project)
        val methodToBeDuplicated = data.psiClass!!.findMethodsByName("strAndChar")[0] as PsiMethod
        val strategySettings = SettingsService.instance.state.testListSettings.highlightStrategies
        for (key in strategySettings.keys) {
            strategySettings[key] = false
        }
        strategySettings["Highlight literals"] = true

        val highlights = service.getHighlights(methodToBeDuplicated).map { it.text }
        val expectedHl = listOf("\"string\"", "'c'")

        TestCase.assertEquals(expectedHl, highlights)
    }

    @Test
    fun testDisableStrategy() {
        val data = getBasicTestInfo("/TestDuplication.java")

        val service = DuplicateTestsService(project)
        val methodToBeDuplicated = data.psiClass!!.findMethodsByName("hasAll")[0] as PsiMethod
        val strategySettings = SettingsService.instance.state.testListSettings.highlightStrategies
        for (key in strategySettings.keys) {
            strategySettings[key] = true
        }
        strategySettings["Highlight literals"] = false

        val highlights = service.getHighlights(methodToBeDuplicated).map { it.text }
        val expectedHl = listOf("0", "0", "1", "p.x")

        TestCase.assertEquals(expectedHl, highlights)
    }
}
