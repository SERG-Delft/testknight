package com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements

import com.intellij.psi.PsiCatchSection
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.exceptions.InvalidConfigurationException
import com.testbuddy.models.testingChecklist.leafNodes.branchingStatements.TryStatementChecklistNode
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

internal class TryStatementChecklistGenerationStrategyTest : BasePlatformTestCase() {

    private val generationStrategy = TryStatementChecklistGenerationStrategy.create()

    @Before
    public override fun setUp() {
        super.setUp()
        this.myFixture.configureByFile("/Person.java")
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testSimpleTry() {
        val method = getMethod("getSpouseName")
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
        val method = getMethod("getSpouseNameNoCatch")
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
        val method = getMethod("getSpouseNameMultipleCatches")
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
        val method = getMethod("getSpouseNameCatchAndFinally")
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
        val method = getMethod("getSpouseNameIncompleteCatch")
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
        val generationStrategy = TryStatementChecklistGenerationStrategy.createFromString("BinaryCaseGeneration")
        val method = getMethod("getSpouseNameMultipleCatches")
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

    private fun getMethod(methodName: String): PsiMethod {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        return testClass!!.findMethodsByName(methodName)[0] as PsiMethod
    }
}
