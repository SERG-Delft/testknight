package com.testbuddy.highlightResolutionStrategies

import com.intellij.psi.PsiMethod
import com.intellij.testFramework.UsefulTestCase
import com.testbuddy.extensions.TestBuddyTestCase
import org.junit.Test

internal class ConstructorArgsStrategyTest : TestBuddyTestCase() {

    @Test
    fun testGetConstructorArgs() {
        val data = getBasicTestInfo("PointTest.java")

        val testMethod = data.psiClass!!.findMethodsByName("translateTest")[0] as PsiMethod
        val toBeChangedTxt = ConstructorArgsStrategy.getElements(testMethod).map { it.text }

        assertContainsElements(toBeChangedTxt, "0", "0", "1", "2")
        UsefulTestCase.assertSize(4, toBeChangedTxt)
    }
}
