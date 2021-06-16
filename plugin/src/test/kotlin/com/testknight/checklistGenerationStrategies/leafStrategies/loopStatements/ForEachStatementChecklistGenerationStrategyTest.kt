package com.testknight.checklistGenerationStrategies.leafStrategies.loopStatements

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testknight.extensions.TestKnightTestCase
import com.testknight.models.testingChecklist.leafNodes.loopStatements.ForEachStatementChecklistNode
import junit.framework.TestCase
import org.junit.Test

internal class ForEachStatementChecklistGenerationStrategyTest : TestKnightTestCase() {

    private val generationStrategy = ForEachStatementChecklistGenerationStrategy.create()

    @Test
    fun testMissingIteratedValueReturnsEmptyList() {
        getBasicTestInfo("/SimpleArray.java")

        val method = getMethodByName("brokenForEach")
        val foreachStatement = PsiTreeUtil.findChildOfType(method, PsiForeachStatement::class.java)

        val expected = emptyList<ForEachStatementChecklistNode>()
        val actual = generationStrategy.generateChecklist(foreachStatement!!)

        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testForeachChecklistGenerationCorrect() {
        getBasicTestInfo("/SimpleArray.java")

        val method = getMethodByName("incrementByOneForEach")
        val foreachStatement = PsiTreeUtil.findChildOfType(method, PsiForeachStatement::class.java)

        val expected = listOf(
            ForEachStatementChecklistNode(description = "Test where getArrayOfInts() is empty", foreachStatement as PsiElement, "getArrayOfInts()"),
            ForEachStatementChecklistNode(description = "Test where getArrayOfInts() has one element", foreachStatement as PsiElement, "getArrayOfInts()"),
            ForEachStatementChecklistNode(description = "Test where getArrayOfInts() is null", foreachStatement as PsiElement, "getArrayOfInts()"),
            ForEachStatementChecklistNode(description = "Test where foreach loop runs multiple times", foreachStatement as PsiElement, "getArrayOfInts()")
        )
        val actual = generationStrategy.generateChecklist(foreachStatement!!)

        TestCase.assertEquals(expected, actual)
    }
}
