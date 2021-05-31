package com.testbuddy.models

import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiExpression
import com.testbuddy.extensions.TestBuddyTestCase
import junit.framework.TestCase
import org.junit.Test

internal class PropositionalExpressionTest : TestBuddyTestCase() {

    @Test
    fun testSimplificationSimple() {
        val psiElementFactory = PsiElementFactory.getInstance(project)
        val expr = psiElementFactory.createExpressionFromText("a && b", null)

        val (simplified, assignments) = PropositionalExpression(expr).simplified()

        TestCase.assertEquals("PROP1 && PROP0", simplified)
        TestCase.assertEquals(assignments["PROP1"], "a")
        TestCase.assertEquals(assignments["PROP0"], "b")
    }

    @Test
    fun testPreorderPolyadic() {
        val psiElementFactory = PsiElementFactory.getInstance(project)
        val expr = psiElementFactory.createExpressionFromText("a && b && c", null)

        val (simplified, assignments) = PropositionalExpression(expr).simplified()

        TestCase.assertEquals("PROP2 && PROP1 && PROP0", simplified)
        TestCase.assertEquals(assignments["PROP2"], "a")
        TestCase.assertEquals(assignments["PROP1"], "b")
        TestCase.assertEquals(assignments["PROP0"], "c")
    }

    @Test
    fun testSimplificationArithmetic() {
        val psiElementFactory = PsiElementFactory.getInstance(project)
        val expr = psiElementFactory.createExpressionFromText("a && (b > c)", null)

        val (simplified, assignments) = PropositionalExpression(expr).simplified()

        TestCase.assertEquals("PROP1 && (PROP0)", simplified)
        TestCase.assertEquals(assignments["PROP0"], "b > c")
        TestCase.assertEquals(assignments["PROP1"], "a")
    }

    @Test
    fun testSimplificationSingleProp() {
        val psiElementFactory = PsiElementFactory.getInstance(project)

        val expr = psiElementFactory.createExpressionFromText("a > b", null) as PsiExpression

        val (simplified, assignments) = PropositionalExpression(expr).simplified()

        TestCase.assertEquals("PROP0", simplified)
        TestCase.assertEquals(assignments["PROP0"], "a > b")
    }

    @Test
    fun testSimplificationLiterals() {
        val psiElementFactory = PsiElementFactory.getInstance(project)

        val expr = psiElementFactory.createExpressionFromText("true && false", null) as PsiExpression

        val (simplified, assignments) = PropositionalExpression(expr).simplified()

        TestCase.assertEquals("true && false", simplified)
    }

    @Test
    fun testSimplificationSingleLiteral() {
        val psiElementFactory = PsiElementFactory.getInstance(project)

        val expr = psiElementFactory.createExpressionFromText("true", null) as PsiExpression

        val (simplified, assignments) = PropositionalExpression(expr).simplified()

        TestCase.assertEquals("true", simplified)
    }

    @Test
    fun testSimplificationComplex() {
        val psiElementFactory = PsiElementFactory.getInstance(project)

        val expr = psiElementFactory.createExpressionFromText(
            "!(a == b) && (b > c) ^ (e || !a.get())",
            null
        )

        val (simplified, assignments) = PropositionalExpression(expr).simplified()

        TestCase.assertEquals("PROP3 && (PROP2) ^ (PROP1 || PROP0)", simplified)
        TestCase.assertEquals(assignments["PROP0"], "!a.get()")
        TestCase.assertEquals(assignments["PROP1"], "e")
        TestCase.assertEquals(assignments["PROP2"], "b > c")
        TestCase.assertEquals(assignments["PROP3"], "!(a == b)")
    }
}
