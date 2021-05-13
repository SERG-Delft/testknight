package com.testbuddy.models

import com.intellij.psi.PsiElementFactory
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.models.PropositionalExpression
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class PropositionalExpressionTest : BasePlatformTestCase() {

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testPreorderSimple() {
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
    fun testPreorderArithmetic() {
        val psiElementFactory = PsiElementFactory.getInstance(project)
        val expr = psiElementFactory.createExpressionFromText("a && (b > c)", null)

        val (simplified, assignments) = PropositionalExpression(expr).simplified()

        TestCase.assertEquals("PROP1 && (PROP0)", simplified)
        TestCase.assertEquals(assignments["PROP0"], "b > c")
        TestCase.assertEquals(assignments["PROP1"], "a")
    }

    @Test
    fun testPreorderComplex() {
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
