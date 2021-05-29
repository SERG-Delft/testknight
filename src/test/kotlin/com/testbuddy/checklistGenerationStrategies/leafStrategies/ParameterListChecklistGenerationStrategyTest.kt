package com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameter
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.ParameterChecklistNode
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class ParameterListChecklistGenerationStrategyTest : BasePlatformTestCase() {

    private val generationStrategy = ParameterListChecklistGenerationStrategy.create()

    @Before
    public override fun setUp() {
        super.setUp()
        this.myFixture.configureByFile("/Person.java")
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testKnownTypeParameters() {
        val method = getMethod("getYearBorn")
        val parameter = PsiTreeUtil.findChildrenOfType(method!!, PsiParameter::class.java).elementAt(0)
        val expected = listOf(
            ParameterChecklistNode("Test method parameter \"currentYear\" equal to: 1", parameter, "currentYear", "1"),
            ParameterChecklistNode("Test method parameter \"currentYear\" equal to: 0", parameter, "currentYear", "0"),
            ParameterChecklistNode("Test method parameter \"currentYear\" equal to: Integer.MAX_VALUE", parameter, "currentYear", "Integer.MAX_VALUE"),
            ParameterChecklistNode("Test method parameter \"currentYear\" equal to: Integer.MIN_VALUE", parameter, "currentYear", "Integer.MIN_VALUE"),
            ParameterChecklistNode("Test method parameter \"currentYear\" equal to: -42", parameter, "currentYear", "-42")
        )
        val actual = generationStrategy.generateChecklistForParameter(parameter)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testUnknownTypeParameters() {
        val method = getMethod("marryTo")
        val parameter = PsiTreeUtil.findChildrenOfType(method!!, PsiParameter::class.java).elementAt(0)
        val expected = emptyList<ParameterChecklistNode>()
        val actual = generationStrategy.generateChecklistForParameter(parameter)
        TestCase.assertEquals(expected, actual)
    }

    private fun getMethod(methodName: String): PsiMethod {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        return testClass!!.findMethodsByName(methodName)[0] as PsiMethod
    }
}
