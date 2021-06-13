package com.testbuddy.highlightResolutionStrategies

import com.intellij.psi.PsiMethod
import com.testbuddy.extensions.TestBuddyTestCase
import junit.framework.TestCase
import org.junit.Test

internal class MagicNumberStrategyTest : TestBuddyTestCase() {

    @Test
    fun testBasic() {
        val data = getBasicTestInfo("PointTest.java")

        val testMethod = data.psiClass!!.findMethodsByName("translateTest")[0] as PsiMethod

        val toBeChangedTxt = MagicNumberStrategy.getElements(testMethod).map { it.text }.toSet()

        TestCase.assertEquals(toBeChangedTxt, setOf("0", "0", "1", "2", "1", "2"))
    }
}
