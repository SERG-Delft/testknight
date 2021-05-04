package com.testbuddy.models

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.models.HighlightableElement
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class HighlightableElementTest : BasePlatformTestCase() {

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testEnablingHighlights() {
        this.myFixture.configureByFile("/PointTest.java")
        val psi = this.myFixture.file
        val editor = this.myFixture.editor

        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        if (testClass != null) {

            // the PSI element of the identifier for the test "translateTest"
            val psiElement = (testClass.findMethodsByName("translateTest")[0] as PsiMethod).nameIdentifier

            val he = HighlightableElement(psiElement!!, editor)

            val highlighter = he.highlight()

            // assert the element is highlighted
            TestCase.assertTrue(he.isHighlighted())
            TestCase.assertTrue(editor.markupModel.allHighlighters.contains(highlighter))

            he.removeHighlight()

            // assert the element is no longer highlighted
            TestCase.assertFalse(he.isHighlighted())
            TestCase.assertFalse(editor.markupModel.allHighlighters.contains(highlighter))
        }
    }

    @Test
    fun testDisablingHighlights() {
        this.myFixture.configureByFile("/PointTest.java")
        val psi = this.myFixture.file
        val editor = this.myFixture.editor

        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        if (testClass != null) {

            // the PSI element of the identifier for the test "translateTest"
            val psiElement = (testClass.findMethodsByName("translateTest")[0] as PsiMethod).nameIdentifier

            val he = HighlightableElement(psiElement!!, editor)

            val highlighter = he.highlight()
            he.removeHighlight()

            // assert the element is no longer highlighted
            TestCase.assertFalse(he.isHighlighted())
            TestCase.assertFalse(editor.markupModel.allHighlighters.contains(highlighter))
        }
    }
}
