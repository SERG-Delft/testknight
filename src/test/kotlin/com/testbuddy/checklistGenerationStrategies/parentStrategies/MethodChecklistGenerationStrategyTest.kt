package com.testbuddy.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.IfStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.SwitchStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.TryStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.ParameterChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.ThrowStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements.DoWhileStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements.ForStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements.WhileStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies.MethodChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode
import com.testbuddy.com.testbuddy.models.TestingChecklistMethodNode
import com.testbuddy.com.testbuddy.utilities.ChecklistLeafNodeGenerator
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class MethodChecklistGenerationStrategyTest : BasePlatformTestCase() {

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testMethodGeneratesOnIfStatements() {
        val leafNodeGenerator = ChecklistLeafNodeGenerator()
        setUpGenerator(leafNodeGenerator)
        val methodGenerator = MethodChecklistGenerationStrategy.create(leafNodeGenerator)
        this.myFixture.configureByFile("/Person.java")
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val methodToGenerateOn = testClass!!.findMethodsByName("setAge")[0] as PsiMethod

        val ifStatements = PsiTreeUtil.findChildrenOfType(methodToGenerateOn, PsiIfStatement::class.java)

        val ifNegativeAge = ifStatements.elementAt(0)
        val ifVeryLargeAge = ifStatements.elementAt(1)

        val ifConditionGenerator = mockk<IfStatementChecklistGenerationStrategy>()
        every { ifConditionGenerator.generateChecklist(ifNegativeAge) } returns
            listOf(
                TestingChecklistLeafNode("Test for age < 0", ifNegativeAge),
                TestingChecklistLeafNode("Test for age == 0", ifNegativeAge),
                TestingChecklistLeafNode("Test for age > 0", ifNegativeAge)
            )
        every { ifConditionGenerator.generateChecklist(ifVeryLargeAge) } returns
            listOf(
                TestingChecklistLeafNode("Test for age > 100", ifNegativeAge),
                TestingChecklistLeafNode("Test for age == 100", ifNegativeAge),
                TestingChecklistLeafNode("Test for age < 100", ifNegativeAge)
            )
        leafNodeGenerator.ifStatementChecklistGenerationStrategy = ifConditionGenerator

        val expectedChildren = listOf(
            TestingChecklistLeafNode("Test for age < 0", ifNegativeAge),
            TestingChecklistLeafNode("Test for age == 0", ifNegativeAge),
            TestingChecklistLeafNode("Test for age > 0", ifNegativeAge),
            TestingChecklistLeafNode("Test for age > 100", ifNegativeAge),
            TestingChecklistLeafNode("Test for age == 100", ifNegativeAge),
            TestingChecklistLeafNode("Test for age < 100", ifNegativeAge)
        )
        val expectedNode = TestingChecklistMethodNode("setAge", expectedChildren, methodToGenerateOn)
        val actualNode = methodGenerator.generateChecklist(methodToGenerateOn)
        TestCase.assertEquals(expectedNode, actualNode)
    }

//    @Test
//    fun testMethodGeneratesOnIfElseIfElse() {}

//    @Test
//    fun testMethodGeneratesOnSwitchStatements() {}

//    @Test
//    fun testMethodGeneratesOnTryStatements() {}

//    @Test
//    fun testMethodGeneratesOnParameters() {}

//    @Test
//    fun testMethodGeneratesOnWhileLoops() {}

//    @Test
//    fun testMethodGeneratesOnForLoops() {}

//    @Test
//    fun testMethodGeneratesOnDoWhileLoops() {}

    // TODO:
//    @Test
//    fun testMethodGeneratesOnForEachLoops() {}

//    @Test
//    fun testMethodGeneratesOnThrowStatements() {}

//    @Test
//    fun testMethodSuccessfullyCombinesSubChecklists() {}

    private fun setUpGenerator(leafNodeGenerator: ChecklistLeafNodeGenerator) {
        val ifStrategy = mockk<IfStatementChecklistGenerationStrategy>()
        val switchStrategy = mockk<SwitchStatementChecklistGenerationStrategy>()
        val tryStrategy = mockk<TryStatementChecklistGenerationStrategy>()
        val parameterStrategy = mockk<ParameterChecklistGenerationStrategy>()
        val whileStrategy = mockk<WhileStatementChecklistGenerationStrategy>()
        val forStrategy = mockk<ForStatementChecklistGenerationStrategy>()
        val doWhileStrategy = mockk<DoWhileStatementChecklistGenerationStrategy>()
        // TODO For Each
        val throwStatement = mockk<ThrowStatementChecklistGenerationStrategy>()

        every { ifStrategy.generateChecklist(any()) } returns emptyList()
        every { switchStrategy.generateChecklist(any()) } returns emptyList()
        every { tryStrategy.generateChecklist(any()) } returns emptyList()
        every { parameterStrategy.generateChecklist(any()) } returns emptyList()
        every { whileStrategy.generateChecklist(any()) } returns emptyList()
        every { forStrategy.generateChecklist(any()) } returns emptyList()
        every { doWhileStrategy.generateChecklist(any()) } returns emptyList()
        every { throwStatement.generateChecklist(any()) } returns emptyList()

        leafNodeGenerator.ifStatementChecklistGenerationStrategy = ifStrategy
        leafNodeGenerator.switchStatementChecklistGenerationStrategy = switchStrategy
        leafNodeGenerator.tryStatementChecklistGenerationStrategy = tryStrategy
        leafNodeGenerator.parameterChecklistGenerationStrategy = parameterStrategy
        leafNodeGenerator.whileStatementChecklistGenerationStrategy = whileStrategy
        leafNodeGenerator.forStatementChecklistGenerationStrategy = forStrategy
        leafNodeGenerator.doWhileStatementChecklistGenerationStrategy = doWhileStrategy
        leafNodeGenerator.throwStatementChecklistGenerationStrategy = throwStatement
    }
}
