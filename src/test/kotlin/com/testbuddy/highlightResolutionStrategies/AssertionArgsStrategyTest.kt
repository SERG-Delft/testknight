package com.testbuddy.highlightResolutionStrategies

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.UsefulTestCase
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.highlightResolutionStrategies.AssertionArgsStrategy
import com.testbuddy.services.TestAnalyzerService
import org.junit.Before
import org.junit.Test

internal class AssertionArgsStrategyTest : BasePlatformTestCase() {

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String = "testdata"

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
