package com.testbuddy.highlightResolutionStrategies

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.highlightResolutionStrategies.MagicNumberStrategy
import com.testbuddy.services.TestAnalyzerService
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class MagicNumberStrategyTest : BasePlatformTestCase() {

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String = "testdata"

    @Test
    fun testBasic() {

        this.myFixture.configureByFile("/PointTest.java")

        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        val testMethod = testClass.findMethodsByName("translateTest")[0] as PsiMethod

        val toBeChangedTxt = MagicNumberStrategy.getElements(testMethod).map { it.text }.toSet()

        TestCase.assertEquals(toBeChangedTxt, setOf("0", "0", "1", "2", "1", "2"))
    }
}
