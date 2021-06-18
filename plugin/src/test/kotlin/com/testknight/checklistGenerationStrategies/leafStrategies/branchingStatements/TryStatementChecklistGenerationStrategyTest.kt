package com.testknight.checklistGenerationStrategies.leafStrategies.branchingStatements

import com.intellij.psi.PsiCatchSection
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testknight.exceptions.InvalidConfigurationException
import com.testknight.extensions.TestKnightTestCase
import com.testknight.models.testingChecklist.leafNodes.branchingStatements.TryStatementChecklistNode
import org.junit.Test
import kotlin.test.assertFailsWith

internal class TryStatementChecklistGenerationStrategyTest : TestKnightTestCase() {

    private val generationStrategy = TryStatementChecklistGenerationStrategy.create()

    @Test
    fun testSimpleTry() {
        getBasicTestInfo("/Person.java")

        val method = getMethodByName("getSpouseName")
        val tryStatement = PsiTreeUtil.findChildOfType(method, PsiTryStatement::class.java)
        val catchStatement = PsiTreeUtil.findChildOfType(tryStatement, PsiCatchSection::class.java)

        val expected = listOf(
            TryStatementChecklistNode("Test with the try block running successfully", tryStatement!!, null),
            TryStatementChecklistNode("Test with the try block throwing a NotMarriedException", catchStatement!!, "NotMarriedException")
        )
        val actual = generationStrategy.generateChecklist(tryStatement!!)

        assertEquals(expected, actual)
    }

    @Test
    fun testTryWithoutCatch() {
        getBasicTestInfo("/Person.java")

        val method = getMethodByName("getSpouseNameNoCatch")
        val tryStatement = PsiTreeUtil.findChildOfType(method, PsiTryStatement::class.java)

        val expected = listOf(
            TryStatementChecklistNode("Test with the try block running successfully", tryStatement!!, null),
            TryStatementChecklistNode("Test with the try block throwing an exception", tryStatement!!, "AnyException")
        )
        val actual = generationStrategy.generateChecklist(tryStatement!!)

        assertEquals(expected, actual)
    }

    @Test
    fun testTryWithMultipleCatches() {
        getBasicTestInfo("/Person.java")

        val method = getMethodByName("getSpouseNameMultipleCatches")
        val tryStatement = PsiTreeUtil.findChildOfType(method, PsiTryStatement::class.java)
        val catches = PsiTreeUtil.findChildrenOfType(tryStatement, PsiCatchSection::class.java)
        val notMarriedCatch = catches.elementAt(0)
        val nullPointerCatch = catches.elementAt(1)

        val expected = listOf(
            TryStatementChecklistNode("Test with the try block running successfully", tryStatement!!, null),
            TryStatementChecklistNode("Test with the try block throwing a NotMarriedException", notMarriedCatch, "NotMarriedException"),
            TryStatementChecklistNode("Test with the try block throwing a NullPointerException", nullPointerCatch, "NullPointerException")
        )
        val actual = generationStrategy.generateChecklist(tryStatement!!)

        assertEquals(expected, actual)
    }

    @Test
    fun testTryWithCatchAndFinally() {
        getBasicTestInfo("/Person.java")

        val method = getMethodByName("getSpouseNameCatchAndFinally")
        val tryStatement = PsiTreeUtil.findChildOfType(method, PsiTryStatement::class.java)
        val catchStatement = PsiTreeUtil.findChildOfType(tryStatement, PsiCatchSection::class.java)

        val expected = listOf(
            TryStatementChecklistNode("Test with the try block running successfully", tryStatement!!, null),
            TryStatementChecklistNode("Test with the try block throwing a NotMarriedException", catchStatement!!, "NotMarriedException")
        )
        val actual = generationStrategy.generateChecklist(tryStatement!!)

        assertEquals(expected, actual)
    }

    @Test
    fun testWithIncompleteCatch() {
        getBasicTestInfo("/Person.java")

        val method = getMethodByName("getSpouseNameIncompleteCatch")
        val tryStatement = PsiTreeUtil.findChildOfType(method, PsiTryStatement::class.java)
        val catchStatement = PsiTreeUtil.findChildOfType(tryStatement, PsiCatchSection::class.java)

        val expected = listOf(
            TryStatementChecklistNode("Test with the try block running successfully", tryStatement!!, null),
            TryStatementChecklistNode("Test with the try block throwing a NotMarriedException", catchStatement!!, "NotMarriedException")
        )
        val actual = generationStrategy.generateChecklist(tryStatement!!)

        assertEquals(expected, actual)
    }

    @Test
    fun testTryMultipleCatchesBinaryGeneration() {
        getBasicTestInfo("/Person.java")

        val generationStrategy = TryStatementChecklistGenerationStrategy.createFromString("BinaryCaseGeneration")
        val method = getMethodByName("getSpouseNameMultipleCatches")
        val tryStatement = PsiTreeUtil.findChildOfType(method, PsiTryStatement::class.java)

        val expected = listOf(
            TryStatementChecklistNode("Test with the try block running successfully", tryStatement!!, null),
            TryStatementChecklistNode("Test with the try block throwing an exception", tryStatement!!, "AnyException")
        )
        val actual = generationStrategy.generateChecklist(tryStatement!!)

        assertEquals(expected, actual)
    }

    @Test
    fun testInvalidStringForCreation() {
        assertFailsWith<InvalidConfigurationException> { TryStatementChecklistGenerationStrategy.createFromString("Foo") }
    }
}
