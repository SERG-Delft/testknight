package com.testknight.highlightResolutionStrategies

import com.intellij.psi.PsiMethod
import com.intellij.testFramework.UsefulTestCase
import com.testknight.extensions.TestKnightTestCase
import org.junit.Test

internal class ConstructorArgsStrategyTest : TestKnightTestCase() {

    @Test
    fun testGetConstructorArgs() {
        val data = getBasicTestInfo("PointTest.java")

        val testMethod = data.psiClass!!.findMethodsByName("translateTest")[0] as PsiMethod
        val toBeChangedTxt = ConstructorArgsStrategy.getElements(testMethod).map { it.text }

        assertContainsElements(toBeChangedTxt, "0", "0", "1", "2")
        UsefulTestCase.assertSize(4, toBeChangedTxt)
    }
}
