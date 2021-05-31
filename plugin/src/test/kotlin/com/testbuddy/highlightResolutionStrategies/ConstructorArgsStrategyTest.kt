package com.testbuddy.highlightResolutionStrategies

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.UsefulTestCase
import com.testbuddy.extensions.TestBuddyTestCase
import org.junit.Test

internal class ConstructorArgsStrategyTest : TestBuddyTestCase() {

    @Test
    fun testGetConstructorArgs() {

        this.myFixture.configureByFile("/PointTest.java")

        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        val testMethod = testClass.findMethodsByName("translateTest")[0] as PsiMethod

        val toBeChangedTxt = ConstructorArgsStrategy.getElements(testMethod).map { it.text }

        assertContainsElements(toBeChangedTxt, "0", "0", "1", "2")
        UsefulTestCase.assertSize(4, toBeChangedTxt)
    }
}
