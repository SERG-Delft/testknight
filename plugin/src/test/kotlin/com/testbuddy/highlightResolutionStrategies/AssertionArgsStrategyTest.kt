package com.testbuddy.highlightResolutionStrategies

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.UsefulTestCase
import com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.services.TestAnalyzerService
import org.junit.Test

internal class AssertionArgsStrategyTest : TestBuddyTestCase() {

    @Test
    fun testBasic() {

        this.myFixture.configureByFile("/PointTest.java")
        val testAnalyzerService = TestAnalyzerService()

        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        val testMethod = testClass.findMethodsByName("translateTest")[0] as PsiMethod

        val toBeChangedTxt = AssertionArgsStrategy.getElements(testMethod).map { it.text }

        assertContainsElements(toBeChangedTxt, "p1", "p2")
        UsefulTestCase.assertSize(2, toBeChangedTxt)
    }
}
