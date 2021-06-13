package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.models.TestMethodData
import junit.framework.TestCase
import org.junit.jupiter.api.Test

class GotoTestServiceTest : TestBuddyTestCase() {

    private val service = GotoTestService()

    @Test
    fun testInitialOffsetZero() {
        val data = getBasicTestInfo("/AnimalTest.java")

        val initialOffset = this.myFixture.editor.scrollingModel.verticalScrollOffset
        val testClass = PsiTreeUtil.findChildOfType(data.psiFile, PsiClass::class.java)

        service.gotoMethod(
            data.editor,
            TestMethodData(
                "setYTest", "AnimalTest",
                psiMethod = testClass!!.findMethodsByName("setSoundTest")[0] as PsiMethod
            )
        )
        val finalOffset = data.editor.scrollingModel.verticalScrollOffset

        TestCase.assertEquals(0, initialOffset) // initial offset without scrolling is 0
        TestCase.assertNotSame(initialOffset, finalOffset) // asserts that scroll position in fact changed
    }

    @Test
    fun testSetSoundTestOffset() {
        val data = getBasicTestInfo("/AnimalTest.java")
        val testClass = PsiTreeUtil.findChildOfType(data.psiFile, PsiClass::class.java)

        service.gotoMethod(
            data.editor,
            TestMethodData(
                "setYTest",
                "AnimalTest", psiMethod = testClass!!.findMethodsByName("setSoundTest")[0] as PsiMethod
            )
        )
        val offsetAfterScrolling = data.editor.scrollingModel.verticalScrollOffset

        TestCase.assertNotSame(0, offsetAfterScrolling)
    }

    @Test
    fun testAnimalSoundOffsetChanged() {
        val data = getBasicTestInfo("/AnimalTest.java")
        val psi = this.myFixture.file
        val editor = this.myFixture.editor
        val initialOffset = this.myFixture.editor.scrollingModel.verticalScrollOffset
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)

        service.gotoMethod(
            editor,
            TestMethodData(
                "setYTest",
                "AnimalTest", psiMethod = testClass!!.findMethodsByName("AnimalSoundTest")[0] as PsiMethod
            )
        )
        val finalOffset = data.editor.scrollingModel.verticalScrollOffset
        TestCase.assertNotSame(initialOffset, finalOffset)
    }
}
