package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Before
import org.junit.Test

internal class TestAnalyzerServiceTest : BasePlatformTestCase() {

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testBasic() {

        this.myFixture.configureByFile("/PointTest.java")
        val testAnalyzerService = TestAnalyzerService()

        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        val testMethod = testClass.findMethodsByName("translateTest")[0] as PsiMethod

        val toBeChangedTxt = testAnalyzerService.getAssertionParameters(testMethod)
            .map { it.text }

        assertContainsElements(toBeChangedTxt, "p1", "p2")
    }
}
