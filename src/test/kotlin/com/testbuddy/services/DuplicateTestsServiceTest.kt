package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.startOffset
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase
import org.junit.Before
import org.junit.jupiter.api.Test

class DuplicateTestsServiceTest : BasePlatformTestCase() {

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testDuplicateMethodUnderCaret() {

        this.myFixture.configureByFile("/PointTest.java")
        val psi = this.myFixture.file
        val editor = this.myFixture.editor
        val project = this.myFixture.project

        val service = DuplicateTestsService(project)
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)

        if (testClass != null) {
            val methodToBeDuplicated = testClass.findMethodsByName("translateTest")[0] as PsiMethod
            editor.caretModel.primaryCaret.moveToOffset(methodToBeDuplicated.startOffset)

            service.duplicateMethodUnderCaret(psi, editor)

            this.myFixture.checkResultByFile(
                "/expected/DuplicateTestsServiceTest.testDuplicateMethodUnderCaret.java"
            )
        }
    }

    @Test
    fun testDuplicateMethod() {

        this.myFixture.configureByFile("/PointTest.java")
        val psi = this.myFixture.file
        val editor = this.myFixture.editor
        val project = this.myFixture.project

        val service = DuplicateTestsService(project)
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)

        if (testClass != null) {
            val methodToBeDuplicated = testClass.findMethodsByName("translateTest")[0] as PsiMethod

            service.duplicateMethod(methodToBeDuplicated, editor)

            this.myFixture.checkResultByFile(
                "/expected/DuplicateTestsServiceTest.testDuplicateMethod.java"
            )
        }
    }

    @Test
    fun testDuplicates() {

        this.myFixture.configureByFile("/TestDuplication.java")

        val service = DuplicateTestsService(project)
        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        val methodToBeDuplicated = testClass.findMethodsByName("duplicate")[0] as PsiMethod

        val highlights = service.getHighlights(methodToBeDuplicated).map { it.text }
        val expectedHl = listOf("0", "1")

        TestCase.assertEquals(expectedHl, highlights)
    }

    @Test
    fun testContaining() {

        this.myFixture.configureByFile("/TestDuplication.java")

        val service = DuplicateTestsService(project)
        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        val methodToBeDuplicated = testClass.findMethodsByName("containing")[0] as PsiMethod

        val highlights = service.getHighlights(methodToBeDuplicated).map { it.text }
        val expectedHl = listOf("0", "1", "2")

        TestCase.assertEquals(expectedHl, highlights)
    }

    @Test
    fun testNestedContaining() {

        this.myFixture.configureByFile("/TestDuplication.java")

        val service = DuplicateTestsService(project)
        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        val methodToBeDuplicated = testClass.findMethodsByName("nestedContains")[0] as PsiMethod

        val highlights = service.getHighlights(methodToBeDuplicated).map { it.text }
        val expectedHl = listOf("0", "1", "2", "3", "dar()")

        TestCase.assertEquals(expectedHl, highlights)
    }
}
