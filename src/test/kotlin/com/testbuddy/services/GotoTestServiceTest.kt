package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.models.TestMethodData
import junit.framework.TestCase
import org.junit.Before
import org.junit.jupiter.api.Test

class GotoTestServiceTest : BasePlatformTestCase() {

    private val service = GotoTestService()

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testInitialOffsetZero() {
        this.myFixture.configureByFile("/AnimalTest.java")
        val psi = this.myFixture.file
        val editor = this.myFixture.editor
        val initialOffset = this.myFixture.editor.scrollingModel.verticalScrollOffset
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        if (testClass != null) {

            service.gotoMethod(
                editor,
                TestMethodData(
                    "setYTest", "AnimalTest",
                    psiMethod = testClass.findMethodsByName("setSoundTest")[0] as PsiMethod
                )
            )
            val finalOffset = editor.scrollingModel.verticalScrollOffset
            TestCase.assertEquals(0, initialOffset) // initial offset without scrolling is 0
            TestCase.assertNotSame(initialOffset, finalOffset) // asserts that scroll position in fact changed
        } else {
            fail()
        }
    }

    @Test
    fun testSetSoundTestOffset() {
        this.myFixture.configureByFile("/AnimalTest.java")
        val psi = this.myFixture.file
        val editor = this.myFixture.editor
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        if (testClass != null) {

            service.gotoMethod(
                editor,
                TestMethodData(
                    "setYTest",
                    "AnimalTest", psiMethod = testClass.findMethodsByName("setSoundTest")[0] as PsiMethod
                )
            )
            val offsetAfterScrolling = this.myFixture.editor.scrollingModel.verticalScrollOffset
            TestCase.assertNotSame(0, offsetAfterScrolling)
        } else {
            fail()
        }
    }

    @Test
    fun testAnimalSoundOffsetChanged() {
        this.myFixture.configureByFile("/AnimalTest.java")
        val psi = this.myFixture.file
        val editor = this.myFixture.editor
        val initialOffset = this.myFixture.editor.scrollingModel.verticalScrollOffset
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        if (testClass != null) {

            service.gotoMethod(
                editor,
                TestMethodData(
                    "setYTest",
                    "AnimalTest", psiMethod = testClass.findMethodsByName("AnimalSoundTest")[0] as PsiMethod
                )
            )
            val finalOffset = this.myFixture.editor.scrollingModel.verticalScrollOffset
            TestCase.assertNotSame(initialOffset, finalOffset)
        } else {
            fail()
        }
    }
}
