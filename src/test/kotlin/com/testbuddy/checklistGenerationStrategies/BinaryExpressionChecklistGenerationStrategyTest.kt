package com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiPolyadicExpression
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.BinaryExpressionChecklistGenerationStrategy
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class BinaryExpressionChecklistGenerationStrategyTest : BasePlatformTestCase() {

    @Before
    override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testPreorderSimple() {
        val strategy = BinaryExpressionChecklistGenerationStrategy(project)
        val psiElementFactory = PsiElementFactory.getInstance(project)

        val expr = psiElementFactory.createExpressionFromText("a && b", null) as PsiPolyadicExpression

        val (simplified, assignments) = strategy.simplifyExpression(expr)

        TestCase.assertEquals("PROP0 && PROP1", simplified)
        TestCase.assertEquals(assignments["PROP0"], "a")
        TestCase.assertEquals(assignments["PROP1"], "b")
    }

    @Test
    fun testPreorderPolyadic() {
        val strategy = BinaryExpressionChecklistGenerationStrategy(project)
        val psiElementFactory = PsiElementFactory.getInstance(project)

        val expr = psiElementFactory.createExpressionFromText("a && b && c", null) as PsiPolyadicExpression

        val (simplified, assignments) = strategy.simplifyExpression(expr)

        TestCase.assertEquals("PROP0 && PROP1 && PROP2", simplified)
        TestCase.assertEquals(assignments["PROP0"], "a")
        TestCase.assertEquals(assignments["PROP1"], "b")
        TestCase.assertEquals(assignments["PROP2"], "c")
    }

    @Test
    fun testPreorderArithmetic() {
        val strategy = BinaryExpressionChecklistGenerationStrategy(project)
        val psiElementFactory = PsiElementFactory.getInstance(project)

        val expr = psiElementFactory.createExpressionFromText("a && (b > c)", null) as PsiPolyadicExpression

        val (simplified, assignments) = strategy.simplifyExpression(expr)

        TestCase.assertEquals("PROP0 && (PROP1)", simplified)
        TestCase.assertEquals(assignments["PROP0"], "a")
        TestCase.assertEquals(assignments["PROP1"], "b > c")
    }

    @Test
    fun testPreorderComplex() {
        val strategy = BinaryExpressionChecklistGenerationStrategy(project)
        val psiElementFactory = PsiElementFactory.getInstance(project)

        val expr = psiElementFactory.createExpressionFromText(
            "!(a == b) && (b > c) && (e || !a.get())",
            null
        ) as PsiPolyadicExpression

        val (simplified, assignments) = strategy.simplifyExpression(expr)

        TestCase.assertEquals("PROP0 && (PROP3) && (PROP1 || PROP2)", simplified)
        TestCase.assertEquals(assignments["PROP0"], "!(a == b)")
        TestCase.assertEquals(assignments["PROP1"], "e")
        TestCase.assertEquals(assignments["PROP2"], "!a.get()")
        TestCase.assertEquals(assignments["PROP3"], "b > c")
    }
}
