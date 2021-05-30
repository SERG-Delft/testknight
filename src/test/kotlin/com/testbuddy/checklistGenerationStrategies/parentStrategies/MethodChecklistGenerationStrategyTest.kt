package com.testbuddy.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiConditionalExpression
import com.intellij.psi.PsiDoWhileStatement
import com.intellij.psi.PsiForStatement
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiSwitchStatement
import com.intellij.psi.PsiThrowStatement
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.PsiWhileStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.checklistGenerationStrategies.leafStrategies.ParameterListChecklistGenerationStrategy
import com.testbuddy.checklistGenerationStrategies.leafStrategies.ThrowStatementChecklistGenerationStrategy
import com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements.ConditionalExpressionChecklistGenerationStrategy
import com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements.IfStatementChecklistGenerationStrategy
import com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements.SwitchStatementChecklistGenerationStrategy
import com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements.TryStatementChecklistGenerationStrategy
import com.testbuddy.checklistGenerationStrategies.leafStrategies.loopStatements.DoWhileStatementChecklistGenerationStrategy
import com.testbuddy.checklistGenerationStrategies.leafStrategies.loopStatements.ForEachStatementChecklistGenerationStrategy
import com.testbuddy.checklistGenerationStrategies.leafStrategies.loopStatements.ForStatementChecklistGenerationStrategy
import com.testbuddy.checklistGenerationStrategies.leafStrategies.loopStatements.WhileStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.models.testingChecklist.leafNodes.ConditionChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import com.testbuddy.utilities.ChecklistLeafNodeGenerator
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase
import org.junit.Test

internal class MethodChecklistGenerationStrategyTest : TestBuddyTestCase() {

    private val recognizedStructs = arrayOf(
        PsiIfStatement::class.java, PsiSwitchStatement::class.java,
        PsiTryStatement::class.java, PsiParameter::class.java,
        PsiWhileStatement::class.java, PsiForStatement::class.java,
        PsiDoWhileStatement::class.java, PsiForeachStatement::class.java,
        PsiThrowStatement::class.java, PsiConditionalExpression::class.java
    )

    @Test
    fun testMethodGeneratesOnIfStatements() {
        val leafNodeGenerator = ChecklistLeafNodeGenerator()
        setUpGenerator(leafNodeGenerator)
        val methodGenerator = MethodChecklistGenerationStrategy.create(recognizedStructs, leafNodeGenerator)
        this.myFixture.configureByFile("/Person.java")
        val methodToGenerateOn = getMethod("setAge")

        val ifStatements = PsiTreeUtil.findChildrenOfType(methodToGenerateOn, PsiIfStatement::class.java)

        val ifNegativeAge = ifStatements.elementAt(0)
        val ifVeryLargeAge = ifStatements.elementAt(1)

        val ifConditionGenerator = mockk<IfStatementChecklistGenerationStrategy>()
        every { ifConditionGenerator.generateChecklist(ifNegativeAge) } returns
            listOf(
                ConditionChecklistNode("Test for age < 0", ifNegativeAge),
                ConditionChecklistNode("Test for age == 0", ifNegativeAge),
                ConditionChecklistNode("Test for age > 0", ifNegativeAge)
            )
        every { ifConditionGenerator.generateChecklist(ifVeryLargeAge) } returns
            listOf(
                ConditionChecklistNode("Test for age > 100", ifNegativeAge),
                ConditionChecklistNode("Test for age == 100", ifNegativeAge),
                ConditionChecklistNode("Test for age < 100", ifNegativeAge)
            )
        leafNodeGenerator.ifStatementChecklistGenerationStrategy = ifConditionGenerator

        val expectedChildren = mutableListOf<TestingChecklistLeafNode>(
            ConditionChecklistNode("Test for age < 0", ifNegativeAge),
            ConditionChecklistNode("Test for age == 0", ifNegativeAge),
            ConditionChecklistNode("Test for age > 0", ifNegativeAge),
            ConditionChecklistNode("Test for age > 100", ifNegativeAge),
            ConditionChecklistNode("Test for age == 100", ifNegativeAge),
            ConditionChecklistNode("Test for age < 100", ifNegativeAge)
        )
        val expectedNode = TestingChecklistMethodNode("setAge", expectedChildren, methodToGenerateOn)
        val actualNode = methodGenerator.generateChecklist(methodToGenerateOn)
        TestCase.assertEquals(expectedNode, actualNode)
    }

    @Test
    fun testMethodGeneratesOnIfElseIfElse() {
        val leafNodeGenerator = ChecklistLeafNodeGenerator()
        setUpGenerator(leafNodeGenerator)
        val methodGenerator = MethodChecklistGenerationStrategy.create(recognizedStructs, leafNodeGenerator)
        this.myFixture.configureByFile("/Person.java")
        val methodToGenerateOn = getMethod("commentOnAgeWithIfs")

        val ifStatements = PsiTreeUtil.findChildrenOfType(methodToGenerateOn, PsiIfStatement::class.java)

        val ifYoung = ifStatements.elementAt(0)
        val ifSerious = ifStatements.elementAt(1)

        val ifStrategy = mockk<IfStatementChecklistGenerationStrategy>()
        every { ifStrategy.generateChecklist(ifYoung) } returns emptyList()
        every { ifStrategy.generateChecklist(ifSerious) } returns emptyList()
        leafNodeGenerator.ifStatementChecklistGenerationStrategy = ifStrategy

        methodGenerator.generateChecklist(methodToGenerateOn)

        verify { ifStrategy.generateChecklist(ifYoung) }
        verify { ifStrategy.generateChecklist(ifSerious) }
    }

    @Test
    fun testMethodGeneratesOnSwitchStatements() {
        val leafNodeGenerator = ChecklistLeafNodeGenerator()
        setUpGenerator(leafNodeGenerator)
        val methodGenerator = MethodChecklistGenerationStrategy.create(recognizedStructs, leafNodeGenerator)
        this.myFixture.configureByFile("/Person.java")
        val methodToGenerateOn = getMethod("commentOnAge")

        val switch = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiSwitchStatement::class.java)

        val switchStrategy = mockk<SwitchStatementChecklistGenerationStrategy>()
        every { switchStrategy.generateChecklist(switch!!) } returns emptyList()
        leafNodeGenerator.switchStatementChecklistGenerationStrategy = switchStrategy

        methodGenerator.generateChecklist(methodToGenerateOn)

        verify { switchStrategy.generateChecklist(switch!!) }
    }

    @Test
    fun testMethodGeneratesOnTryStatements() {
        val leafNodeGenerator = ChecklistLeafNodeGenerator()
        setUpGenerator(leafNodeGenerator)
        val methodGenerator = MethodChecklistGenerationStrategy.create(recognizedStructs, leafNodeGenerator)
        this.myFixture.configureByFile("/Person.java")
        val methodToGenerateOn = getMethod("getSpouseNameMultipleCatches")

        val tryStatement = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiTryStatement::class.java)

        val tryStrategy = mockk<TryStatementChecklistGenerationStrategy>()
        every { tryStrategy.generateChecklist(tryStatement!!) } returns emptyList()
        leafNodeGenerator.tryStatementChecklistGenerationStrategy = tryStrategy

        methodGenerator.generateChecklist(methodToGenerateOn)

        verify { tryStrategy.generateChecklist(tryStatement!!) }
    }

    @Test
    fun testMethodGeneratesOnWhileLoops() {
        val leafNodeGenerator = ChecklistLeafNodeGenerator()
        setUpGenerator(leafNodeGenerator)
        val methodGenerator = MethodChecklistGenerationStrategy.create(recognizedStructs, leafNodeGenerator)
        this.myFixture.configureByFile("/Person.java")
        val methodToGenerateOn = getMethod("countToTen")

        val whileStatement = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiWhileStatement::class.java)

        val whileStrategy = mockk<WhileStatementChecklistGenerationStrategy>()
        every { whileStrategy.generateChecklist(whileStatement!!) } returns emptyList()
        leafNodeGenerator.whileStatementChecklistGenerationStrategy = whileStrategy

        methodGenerator.generateChecklist(methodToGenerateOn)

        verify { whileStrategy.generateChecklist(whileStatement!!) }
    }

    @Test
    fun testMethodGeneratesOnForLoops() {
        val leafNodeGenerator = ChecklistLeafNodeGenerator()
        setUpGenerator(leafNodeGenerator)
        val methodGenerator = MethodChecklistGenerationStrategy.create(recognizedStructs, leafNodeGenerator)
        this.myFixture.configureByFile("/Person.java")
        val methodToGenerateOn = getMethod("spellName")

        val forStatement = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiForStatement::class.java)

        val forStrategy = mockk<ForStatementChecklistGenerationStrategy>()
        every { forStrategy.generateChecklist(forStatement!!) } returns emptyList()
        leafNodeGenerator.forStatementChecklistGenerationStrategy = forStrategy

        methodGenerator.generateChecklist(methodToGenerateOn)

        verify { forStrategy.generateChecklist(forStatement!!) }
    }

    @Test
    fun testMethodGeneratesOnDoWhileLoops() {
        val leafNodeGenerator = ChecklistLeafNodeGenerator()
        setUpGenerator(leafNodeGenerator)
        val methodGenerator = MethodChecklistGenerationStrategy.create(recognizedStructs, leafNodeGenerator)
        this.myFixture.configureByFile("/Person.java")
        val methodToGenerateOn = getMethod("doWhileExample")

        val doWhile = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiDoWhileStatement::class.java)

        val doWhileStrategy = mockk<DoWhileStatementChecklistGenerationStrategy>()
        every { doWhileStrategy.generateChecklist(doWhile!!) } returns emptyList()
        leafNodeGenerator.doWhileStatementChecklistGenerationStrategy = doWhileStrategy

        methodGenerator.generateChecklist(methodToGenerateOn)

        verify { doWhileStrategy.generateChecklist(doWhile!!) }
    }

    @Test
    fun testMethodGeneratesOnForEachLoops() {
        val leafNodeGenerator = ChecklistLeafNodeGenerator()
        setUpGenerator(leafNodeGenerator)
        val methodGenerator = MethodChecklistGenerationStrategy.create(recognizedStructs, leafNodeGenerator)
        this.myFixture.configureByFile("/Person.java")
        val methodToGenerateOn = getMethod("spellWithForEach")

        val forEach = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiForeachStatement::class.java)

        val forEachStrategy = mockk<ForEachStatementChecklistGenerationStrategy>()
        every { forEachStrategy.generateChecklist(forEach!!) } returns emptyList()
        leafNodeGenerator.forEachStatementChecklistGenerationStrategy = forEachStrategy

        methodGenerator.generateChecklist(methodToGenerateOn)

        verify { forEachStrategy.generateChecklist(forEach!!) }
    }

    @Test
    fun testMethodGeneratesOnTernaryOperator() {
        val leafNodeGenerator = ChecklistLeafNodeGenerator()
        setUpGenerator(leafNodeGenerator)
        val methodGenerator = MethodChecklistGenerationStrategy.create(recognizedStructs, leafNodeGenerator)
        this.myFixture.configureByFile("/Person.java")
        val methodToGenerateOn = getMethod("getSpouseWithTernary")

        val ternary = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiConditionalExpression::class.java)

        val ternaryStrategy = mockk<ConditionalExpressionChecklistGenerationStrategy>()
        every { ternaryStrategy.generateChecklist(ternary!!) } returns emptyList()
        leafNodeGenerator.conditionalExpressionChecklistGenerationStrategy = ternaryStrategy

        methodGenerator.generateChecklist(methodToGenerateOn)

        verify { ternaryStrategy.generateChecklist(ternary!!) }
    }

    @Test
    fun testMethodGeneratesOnThrowStatements() {
        val leafNodeGenerator = ChecklistLeafNodeGenerator()
        setUpGenerator(leafNodeGenerator)
        val methodGenerator = MethodChecklistGenerationStrategy.create(recognizedStructs, leafNodeGenerator)
        this.myFixture.configureByFile("/Person.java")
        val methodToGenerateOn = getMethod("getSpouse")

        val throwStatement = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiThrowStatement::class.java)

        val throwStrategy = mockk<ThrowStatementChecklistGenerationStrategy>()
        every { throwStrategy.generateChecklist(throwStatement!!) } returns emptyList()
        leafNodeGenerator.throwStatementChecklistGenerationStrategy = throwStrategy

        methodGenerator.generateChecklist(methodToGenerateOn)

        verify { throwStrategy.generateChecklist(throwStatement!!) }
    }

    private fun setUpGenerator(leafNodeGenerator: ChecklistLeafNodeGenerator) {
        val ifStrategy = mockk<IfStatementChecklistGenerationStrategy>()
        val switchStrategy = mockk<SwitchStatementChecklistGenerationStrategy>()
        val tryStrategy = mockk<TryStatementChecklistGenerationStrategy>()
        val parameterStrategy = mockk<ParameterListChecklistGenerationStrategy>()
        val whileStrategy = mockk<WhileStatementChecklistGenerationStrategy>()
        val forStrategy = mockk<ForStatementChecklistGenerationStrategy>()
        val doWhileStrategy = mockk<DoWhileStatementChecklistGenerationStrategy>()
        val forEachStrategy = mockk<ForEachStatementChecklistGenerationStrategy>()
        val ternaryStrategy = mockk<ConditionalExpressionChecklistGenerationStrategy>()
        val throwStatement = mockk<ThrowStatementChecklistGenerationStrategy>()

        every { ifStrategy.generateChecklist(any()) } returns emptyList()
        every { switchStrategy.generateChecklist(any()) } returns emptyList()
        every { tryStrategy.generateChecklist(any()) } returns emptyList()
        every { parameterStrategy.generateChecklistForParameter(any()) } returns emptyList()
        every { whileStrategy.generateChecklist(any()) } returns emptyList()
        every { forStrategy.generateChecklist(any()) } returns emptyList()
        every { doWhileStrategy.generateChecklist(any()) } returns emptyList()
        every { throwStatement.generateChecklist(any()) } returns emptyList()
        every { forEachStrategy.generateChecklist(any()) } returns emptyList()
        every { ternaryStrategy.generateChecklist(any()) } returns emptyList()

        leafNodeGenerator.ifStatementChecklistGenerationStrategy = ifStrategy
        leafNodeGenerator.switchStatementChecklistGenerationStrategy = switchStrategy
        leafNodeGenerator.tryStatementChecklistGenerationStrategy = tryStrategy
        leafNodeGenerator.parameterChecklistGenerationStrategy = parameterStrategy
        leafNodeGenerator.whileStatementChecklistGenerationStrategy = whileStrategy
        leafNodeGenerator.forStatementChecklistGenerationStrategy = forStrategy
        leafNodeGenerator.doWhileStatementChecklistGenerationStrategy = doWhileStrategy
        leafNodeGenerator.throwStatementChecklistGenerationStrategy = throwStatement
        leafNodeGenerator.forEachStatementChecklistGenerationStrategy = forEachStrategy
        leafNodeGenerator.conditionalExpressionChecklistGenerationStrategy = ternaryStrategy
    }

    private fun getMethod(methodName: String): PsiMethod {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        return testClass!!.findMethodsByName(methodName)[0] as PsiMethod
    }
}