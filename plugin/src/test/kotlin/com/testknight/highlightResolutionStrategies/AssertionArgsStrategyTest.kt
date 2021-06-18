package com.testknight.highlightResolutionStrategies

import com.intellij.psi.PsiMethod
import com.intellij.testFramework.UsefulTestCase
import com.testknight.extensions.TestKnightTestCase
import org.junit.Test

internal class AssertionArgsStrategyTest : TestKnightTestCase() {

    @Test
    fun testBasic() {
        val data = getBasicTestInfo("PointTest.java")

        val testMethod = data.psiClass!!.findMethodsByName("translateTest")[0] as PsiMethod

        val toBeChangedTxt = AssertionArgsStrategy.getElements(testMethod).map { it.text }

        assertContainsElements(toBeChangedTxt, "p1", "p2")
        UsefulTestCase.assertSize(2, toBeChangedTxt)
    }
}
