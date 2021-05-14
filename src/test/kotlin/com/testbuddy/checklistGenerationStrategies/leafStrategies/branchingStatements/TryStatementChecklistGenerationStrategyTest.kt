package com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements

import com.intellij.psi.PsiCatchSection
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.TryStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.math.exp

internal class TryStatementChecklistGenerationStrategyTest : BasePlatformTestCase() {

    val generationStrategy = TryStatementChecklistGenerationStrategy.create()

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
            TestingChecklistLeafNode("Test with the try block running successfully",tryStatement!!),
            TestingChecklistLeafNode("Test with the try block throwing a NotMarriedException", catchStatement!!)
        )
        val actual = generationStrategy.generateChecklist(tryStatement!!)
        assertEquals(expected, actual)
    }


    @Test
    fun testTryWithoutCatch() {
//        val expected = listOf(
//            TestingChecklistLeafNode("Test with the try block running successfully",tryStatement!!),
//            TestingChecklistLeafNode("Test with the try block throwing an exception", tryStatement!!)
//        )
    }

    @Test
    fun testTryWithMultipleCatches() {}

    @Test
    fun testTryWithCatchAndFinally() {}

    @Test
    fun testWithIncompleteCatch() { /*... catch ( ) {} -> Should ignore it*/}

    private fun getMethod(methodName: String): PsiMethod {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        return testClass!!.findMethodsByName(methodName)[0] as PsiMethod
    }

}