package com.testknight.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiThrowStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testknight.extensions.TestKnightTestCase
import com.testknight.models.testingChecklist.leafNodes.ThrowStatementChecklistNode
import junit.framework.TestCase
import org.junit.Test

internal class ThrowStatementChecklistGenerationStrategyTest : TestKnightTestCase() {

    private val generationStrategy = ThrowStatementChecklistGenerationStrategy.create()

    @Test
    fun testSimpleThrow() {
        getBasicTestInfo("/Person.java")

        val method = getMethodByName("getSpouse")
        val throwStatement = PsiTreeUtil.findChildOfType(method, PsiThrowStatement::class.java)

        val expected = listOf(ThrowStatementChecklistNode("Test when NotMarriedException is thrown", throwStatement!!, "NotMarriedException"))
        val actual = generationStrategy.generateChecklist(throwStatement)

        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testInvalidNewExpressionInThrow() {
        getBasicTestInfo("/Person.java")

        val method = getMethodByName("methodWithBrokenThrows")
        val throwStatement = PsiTreeUtil.findChildrenOfType(method, PsiThrowStatement::class.java).elementAt(0)

        val expected = emptyList<ThrowStatementChecklistNode>()
        val actual = generationStrategy.generateChecklist(throwStatement)

        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testMissingExceptionClassInThrow() {
        getBasicTestInfo("/Person.java")

        val method = getMethodByName("methodWithBrokenThrows")
        val throwStatement = PsiTreeUtil.findChildrenOfType(method, PsiThrowStatement::class.java).elementAt(1)

        val expected = emptyList<ThrowStatementChecklistNode>()
        val actual = generationStrategy.generateChecklist(throwStatement)

        TestCase.assertEquals(expected, actual)
    }
}
