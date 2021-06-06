package com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameter
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.models.testingChecklist.leafNodes.ParameterChecklistNode
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class ParameterListChecklistGenerationStrategyTest : TestBuddyTestCase() {

    private val typeCaseMap = mutableMapOf(
        "byte" to listOf("Byte.MAX_VALUE", "Byte.MIN_VALUE"),
        "short" to listOf("Short.MAX_VALUE", "Short.MIN_VALUE"),
        "int" to listOf("1", "0", "Integer.MAX_VALUE", "Integer.MIN_VALUE", "-42"),
        "long" to listOf("0"),
        "float" to listOf("3.14", "0.0", "-3.14", "1.0", "0.00000042", "Float.MAX_VALUE", "Float.MIN_VALUE"),
        "double" to listOf("Double.MAX_VALUE", "Double.MIN_VALUE"),
        "char" to listOf("a", "α", "∅"),
        "boolean" to listOf("true", "false"),
        "String" to listOf("\"\"", "\"a\"", "\"hello world\"", "\"καλήν εσπέραν άρχοντες\"", "\"€\"", "\"₹\""),
        "byte[]" to listOf("[]", "null"),
        "short[]" to listOf("[]", "null"),
        "int[]" to listOf("null", "[1,2,3,4]", "[4,3,2,1]", "[]"),
        "long[]" to listOf("[]", "null"),
        "float[]" to listOf("[]", "null"),
        "double[]" to listOf("[]", "null"),
        "char[]" to listOf("[]", "null"),
        "boolean[]" to listOf("[]", "null"),
        "String[]" to listOf("[]", "null")
    )

    private val generationStrategy = ParameterListChecklistGenerationStrategy.create(typeCaseMap)

    @Before
    public override fun setUp() {
        super.setUp()
        this.myFixture.configureByFile("/Person.java")
    }

    @Test
    fun testKnownTypeParameters() {
        val method = getMethod("getYearBorn")
        val parameter = PsiTreeUtil.findChildrenOfType(method!!, PsiParameter::class.java).elementAt(0)
        val expected = listOf(
            ParameterChecklistNode("Test method parameter \"currentYear\" equal to: 1", parameter, "currentYear", "1"),
            ParameterChecklistNode("Test method parameter \"currentYear\" equal to: 0", parameter, "currentYear", "0"),
            ParameterChecklistNode(
                "Test method parameter \"currentYear\" equal to: Integer.MAX_VALUE",
                parameter,
                "currentYear",
                "Integer.MAX_VALUE"
            ),
            ParameterChecklistNode(
                "Test method parameter \"currentYear\" equal to: Integer.MIN_VALUE",
                parameter,
                "currentYear",
                "Integer.MIN_VALUE"
            ),
            ParameterChecklistNode(
                "Test method parameter \"currentYear\" equal to: -42",
                parameter,
                "currentYear",
                "-42"
            )
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
