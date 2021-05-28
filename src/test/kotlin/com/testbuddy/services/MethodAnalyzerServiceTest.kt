package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.models.sideEffectAnalysis.*
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import java.lang.AssertionError

class MethodAnalyzerServiceTest : BasePlatformTestCase() {

    private val service = MethodAnalyzerService()

    @Before
    public override fun setUp() {
        super.setUp()
        this.myFixture.configureByFile("/Person.java")
    }

    @Test
    fun testFieldMutationSideEffectsThisNotSpecified() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(ReassignsClassFieldSideEffect("name"))
        assertMethodSideEffects(testClass, expected, "setFullName")
    }

    @Test
    fun testOnlyArgumentMutationSideEffectsThisSpecified() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(ReassignsClassFieldSideEffect("name"))
        assertMethodSideEffects(testClass, expected, "setName")
    }

    @Test
    fun testNoSideEffect() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf<SideEffect>()
        assertMethodSideEffects(testClass, expected, "greet")
    }

    @Test
    fun testMethodCallOnParameter() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            MethodCallOnParameterSideEffect("list", "reverse")
        )
        assertMethodSideEffects(testClass, expected, "reversesList")
    }

    @Test
    fun testMethodCallWithParameterPassed() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            MethodCallOnParameterSideEffect("list", "reverse")
        )
        assertMethodSideEffects(testClass, expected, "reversesListWithArgument")
    }

    @Test
    fun testMethodCallWithBothReferenceAndPrimitiveParameters() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            MethodCallOnParameterSideEffect("msg", "append")
        )
        assertMethodSideEffects(testClass, expected, "sayMyAge")
    }

    @Test
    fun testMethodCallWithMultipleParameters() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            MethodCallOnParameterSideEffect("list", "add"),
            MethodCallOnParameterSideEffect("person", "add")
        )
        assertMethodSideEffects(testClass, expected, "addToList")
    }

    @Test
    fun testMultipleMethodCallWithParametersPassed() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            MethodCallOnParameterSideEffect("list", "add"),
            MethodCallOnParameterSideEffect("person", "add"),
            MethodCallOnParameterSideEffect("list", "reverse")
        )
        assertMethodSideEffects(testClass, expected, "listEditor")
    }

    @Test
    fun testMethodCallWithShadowedParameter() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            MethodCallOnParameterSideEffect("name", "reverse")
        )
        assertMethodSideEffects(testClass, expected, "reverseName")
    }

    @Test
    fun testMethodCallOnClassFieldWithoutThis() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(MethodCallOnClassFieldSideEffect("name", "methodSecond"))
        assertMethodSideEffects(testClass, expected, "methodCallWithoutThis")
    }

    @Test
    fun testMethodCallOnClassFieldWithThis() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(MethodCallOnClassFieldSideEffect("name", "methodSecond"))
        assertMethodSideEffects(testClass, expected, "methodCallWithThis")
    }

    @Test
    fun testMethodCallWithClassFieldArguments() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            MethodCallOnClassFieldSideEffect("spouse", "mysteriousMethod"),
            MethodCallOnClassFieldSideEffect("name", "mysteriousMethod")
        )
        assertMethodSideEffects(testClass, expected, "methodCallWithThisAndWithout")
    }

    @Test
    fun testMultipleMethodCalls() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            MethodCallOnClassFieldSideEffect("name", "toLowerCase"),
            MethodCallOnClassFieldSideEffect("spouse", "mysteriousMethod"),
            MethodCallOnClassFieldSideEffect("name", "mysteriousMethod")
        )
        assertMethodSideEffects(testClass, expected, "multipleMethodCall")
    }

    @Test
    fun testPrimitiveClassFieldPassed() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = emptyList<SideEffect>()
        assertMethodSideEffects(testClass, expected, "powMyAge")
    }

    @Test
    fun testStaticCall() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = emptyList<SideEffect>()
        assertMethodSideEffects(testClass, expected, "powMyAgeStatic")
    }

    @Test
    fun testMethodCallOnThis() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            MethodCallOnClassFieldSideEffect("this", "setName"),
        )
        assertMethodSideEffects(testClass, expected, "setter")
    }

    @Test
    fun testLiteralPassedAsArgument() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            MethodCallOnParameterSideEffect("name", "append"),
        )
        assertMethodSideEffects(testClass, expected, "callWithLiteral")
    }

    @Test
    fun testDuplicateSideEffectsAreRemoved() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            MethodCallOnParameterSideEffect("spouse", "mysteriousMethod"),
        )
        assertMethodSideEffects(testClass, expected, "passParameterTwice")
    }

    @Test
    fun testChainedMethodCallOnArgument() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            MethodCallOnParameterSideEffect("string", "reverse"),
        )
        assertMethodSideEffects(testClass, expected, "chainedMethodCallOnArgument")
    }

    @Test
    fun testChainedMethodCallOnThis() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            MethodCallOnClassFieldSideEffect("this", "method"),
        )
        assertMethodSideEffects(testClass, expected, "chainedMethodCallOnThis")
    }

    @Test
    fun testChainedMethodCallOnThisField() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            MethodCallOnClassFieldSideEffect("name", "append"),
        )
        assertMethodSideEffects(testClass, expected, "chainedMethodCallOnThisField")
    }

    @Test
    fun testChainedStaticMethodCall() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = emptyList<SideEffect>()
        assertMethodSideEffects(testClass, expected, "chainedStaticMethodCall")
    }

    @Test
    fun testDijkstra() {
        this.myFixture.configureByFile("/Dijkstra.java")
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = emptyList<SideEffect>()
        assertMethodSideEffects(testClass, expected, "dijkstra")
    }

    @Test
    fun testReferenceChanged() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf<SideEffect>(
            ReassignsClassFieldSideEffect("spouse"),
            ReassignmentOfTransitiveField("spouse", "spouse")
        )
        assertMethodSideEffects(testClass, expected, "marryToReferenceChanged")
    }

    @Test
    fun testDoubleShadowing() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            ReassignsClassFieldSideEffect("spouse"),
            ParameterFieldReassignmentSideEffect("spouse", "spouse")
        )
        assertMethodSideEffects(testClass, expected, "marryToDoubleShadowing")
    }

    @Test
    fun testParameterFieldAffected() {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = listOf(
            ReassignsClassFieldSideEffect("spouse"),
            ParameterFieldReassignmentSideEffect("newSpouse", "spouse")
        )
        assertMethodSideEffects(testClass, expected, "marryToParameterFieldAffected")

    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    /**
     * This is a helper method to assert that a method has certain side-effects.
     *
     * @param testClass the PsiClass (nullable) that contains the method analyzed.
     * @param expected the expected list of side-effects.
     * @param methodName the name of the method that we want to take the side-effect.
     */
    private fun assertMethodSideEffects(testClass: PsiClass?, expected: List<SideEffect>, methodName: String) {
        if (testClass != null) {
            val method = testClass.findMethodsByName(methodName)[0] as PsiMethod
            val actual = service.getSideEffects(method)
            TestCase.assertEquals(expected, actual)
        } else {
            throw AssertionError("testClass was null")
        }
    }
}
