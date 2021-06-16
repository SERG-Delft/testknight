package com.testknight.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testknight.extensions.TestKnightTestCase
import com.testknight.models.sideEffectAnalysis.MethodCallOnClassFieldSideEffect
import com.testknight.models.sideEffectAnalysis.MethodCallOnParameterSideEffect
import com.testknight.models.sideEffectAnalysis.ParameterFieldReassignmentSideEffect
import com.testknight.models.sideEffectAnalysis.ReassignmentOfTransitiveField
import com.testknight.models.sideEffectAnalysis.ReassignsClassFieldSideEffect
import com.testknight.models.sideEffectAnalysis.SideEffect
import junit.framework.TestCase
import org.junit.Test
import java.lang.AssertionError

class MethodAnalyzerServiceTest : TestKnightTestCase() {

    private val service = MethodAnalyzerService()

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

    @Test
    fun testFieldMutationSideEffectsThisNotSpecified() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(ReassignsClassFieldSideEffect("name"))
        assertMethodSideEffects(data.psiClass, expected, "setFullName")
    }

    @Test
    fun testOnlyArgumentMutationSideEffectsThisSpecified() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(ReassignsClassFieldSideEffect("name"))
        assertMethodSideEffects(data.psiClass, expected, "setName")
    }

    @Test
    fun testNoSideEffect() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf<SideEffect>()
        assertMethodSideEffects(data.psiClass, expected, "greet")
    }

    @Test
    fun testMethodCallOnParameter() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(
            MethodCallOnParameterSideEffect("list", "reverse")
        )
        assertMethodSideEffects(data.psiClass, expected, "reversesList")
    }

    @Test
    fun testMethodCallWithParameterPassed() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(
            MethodCallOnParameterSideEffect("list", "reverse")
        )
        assertMethodSideEffects(data.psiClass, expected, "reversesListWithArgument")
    }

    @Test
    fun testMethodCallWithBothReferenceAndPrimitiveParameters() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(
            MethodCallOnParameterSideEffect("msg", "append")
        )
        assertMethodSideEffects(data.psiClass, expected, "sayMyAge")
    }

    @Test
    fun testMethodCallWithMultipleParameters() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(
            MethodCallOnParameterSideEffect("list", "add"),
            MethodCallOnParameterSideEffect("person", "add")
        )
        assertMethodSideEffects(data.psiClass, expected, "addToList")
    }

    @Test
    fun testMultipleMethodCallWithParametersPassed() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(
            MethodCallOnParameterSideEffect("list", "add"),
            MethodCallOnParameterSideEffect("person", "add"),
            MethodCallOnParameterSideEffect("list", "reverse")
        )
        assertMethodSideEffects(data.psiClass, expected, "listEditor")
    }

    @Test
    fun testMethodCallWithShadowedParameter() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(
            MethodCallOnParameterSideEffect("name", "reverse")
        )
        assertMethodSideEffects(data.psiClass, expected, "reverseName")
    }

    @Test
    fun testMethodCallOnClassFieldWithoutThis() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(MethodCallOnClassFieldSideEffect("name", "methodSecond"))
        assertMethodSideEffects(data.psiClass, expected, "methodCallWithoutThis")
    }

    @Test
    fun testMethodCallOnClassFieldWithThis() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(MethodCallOnClassFieldSideEffect("name", "methodSecond"))
        assertMethodSideEffects(data.psiClass, expected, "methodCallWithThis")
    }

    @Test
    fun testMethodCallWithClassFieldArguments() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(
            MethodCallOnClassFieldSideEffect("spouse", "mysteriousMethod"),
            MethodCallOnClassFieldSideEffect("name", "mysteriousMethod")
        )
        assertMethodSideEffects(data.psiClass, expected, "methodCallWithThisAndWithout")
    }

    @Test
    fun testMultipleMethodCalls() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(
            MethodCallOnClassFieldSideEffect("name", "toLowerCase"),
            MethodCallOnClassFieldSideEffect("spouse", "mysteriousMethod"),
            MethodCallOnClassFieldSideEffect("name", "mysteriousMethod")
        )
        assertMethodSideEffects(data.psiClass, expected, "multipleMethodCall")
    }

    @Test
    fun testPrimitiveClassFieldPassed() {
        val data = getBasicTestInfo("/Person.java")
        val expected = emptyList<SideEffect>()
        assertMethodSideEffects(data.psiClass, expected, "powMyAge")
    }

    @Test
    fun testStaticCall() {
        val data = getBasicTestInfo("/Person.java")
        val expected = emptyList<SideEffect>()
        assertMethodSideEffects(data.psiClass, expected, "powMyAgeStatic")
    }

    @Test
    fun testMethodCallOnThis() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(
            MethodCallOnClassFieldSideEffect("this", "setName"),
        )
        assertMethodSideEffects(data.psiClass, expected, "setter")
    }

    @Test
    fun testLiteralPassedAsArgument() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(
            MethodCallOnParameterSideEffect("name", "append"),
        )
        assertMethodSideEffects(data.psiClass, expected, "callWithLiteral")
    }

    @Test
    fun testDuplicateSideEffectsAreRemoved() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(
            MethodCallOnParameterSideEffect("spouse", "mysteriousMethod"),
        )
        assertMethodSideEffects(data.psiClass, expected, "passParameterTwice")
    }

    @Test
    fun testChainedMethodCallOnArgument() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(
            MethodCallOnParameterSideEffect("string", "reverse"),
        )
        assertMethodSideEffects(data.psiClass, expected, "chainedMethodCallOnArgument")
    }

    @Test
    fun testChainedMethodCallOnThis() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(
            MethodCallOnClassFieldSideEffect("this", "method"),
        )
        assertMethodSideEffects(data.psiClass, expected, "chainedMethodCallOnThis")
    }

    @Test
    fun testChainedMethodCallOnThisField() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(
            MethodCallOnClassFieldSideEffect("name", "append"),
        )
        assertMethodSideEffects(data.psiClass, expected, "chainedMethodCallOnThisField")
    }

    @Test
    fun testChainedStaticMethodCall() {
        val data = getBasicTestInfo("/Person.java")
        val expected = emptyList<SideEffect>()
        assertMethodSideEffects(data.psiClass, expected, "chainedStaticMethodCall")
    }

    @Test
    fun testReferenceChanged() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf<SideEffect>(
            ReassignsClassFieldSideEffect("spouse"),
            ReassignmentOfTransitiveField("spouse", "spouse")
        )
        assertMethodSideEffects(data.psiClass, expected, "marryToReferenceChanged")
    }

    @Test
    fun testDoubleShadowing() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(
            ReassignsClassFieldSideEffect("spouse"),
            ParameterFieldReassignmentSideEffect("spouse", "spouse")
        )
        assertMethodSideEffects(data.psiClass, expected, "marryToDoubleShadowing")
    }

    @Test
    fun testParameterFieldAffected() {
        val data = getBasicTestInfo("/Person.java")
        val expected = listOf(
            ReassignsClassFieldSideEffect("spouse"),
            ParameterFieldReassignmentSideEffect("newSpouse", "spouse")
        )
        assertMethodSideEffects(data.psiClass, expected, "marryToParameterFieldAffected")
    }

    @Test
    fun testDijkstra() {
        val data = getBasicTestInfo("/Dijkstra.java")
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = emptyList<SideEffect>()
        assertMethodSideEffects(testClass, expected, "dijkstra")
    }
}
