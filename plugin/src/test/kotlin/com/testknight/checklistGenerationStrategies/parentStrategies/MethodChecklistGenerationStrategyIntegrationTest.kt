package com.testknight.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiCatchSection
import com.intellij.psi.PsiDoWhileStatement
import com.intellij.psi.PsiForStatement
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiParenthesizedExpression
import com.intellij.psi.PsiPrefixExpression
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiSwitchLabelStatement
import com.intellij.psi.PsiSwitchStatement
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.PsiWhileStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testknight.extensions.TestKnightTestCase
import com.testknight.models.testingChecklist.leafNodes.ConditionChecklistNode
import com.testknight.models.testingChecklist.leafNodes.ParameterChecklistNode
import com.testknight.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testknight.models.testingChecklist.leafNodes.branchingStatements.SwitchStatementChecklistNode
import com.testknight.models.testingChecklist.leafNodes.branchingStatements.TryStatementChecklistNode
import com.testknight.models.testingChecklist.leafNodes.loopStatements.DoWhileStatementChecklistNode
import com.testknight.models.testingChecklist.leafNodes.loopStatements.ForEachStatementChecklistNode
import com.testknight.models.testingChecklist.leafNodes.loopStatements.ForLoopStatementChecklistNode
import com.testknight.models.testingChecklist.leafNodes.loopStatements.WhileStatementChecklistNode
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import org.junit.Test

internal class MethodChecklistGenerationStrategyIntegrationTest : TestKnightTestCase() {

    @Test
    fun testTernaryOperator() {
        getBasicTestInfo("/Methods.java")

        val methodGenerator = MethodChecklistGenerationStrategy.create()
        val methodToGenerateOn = getMethodByName("ternary")
        val expr = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiParenthesizedExpression::class.java)
        val expectedChildren = mutableListOf<TestingChecklistLeafNode>(
            ConditionChecklistNode(
                "Test where \"a > b\" is false",
                expr!!
            ),
            ConditionChecklistNode(
                "Test where \"a > b\" is true",
                expr!!
            )
        )

        val expectedNode = TestingChecklistMethodNode("ternary", expectedChildren, methodToGenerateOn)
        val actual = methodGenerator.generateChecklist(methodToGenerateOn)

        assertEquals(expectedNode, actual)
    }

    @Test
    fun testIfStatement() {
        getBasicTestInfo("/Methods.java")

        val methodGenerator = MethodChecklistGenerationStrategy.create()
        val methodToGenerateOn = getMethodByName("twoBranchesNoParameter")

        val expr = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiReferenceExpression::class.java)
        val expectedChildren = mutableListOf<TestingChecklistLeafNode>(
            ConditionChecklistNode(
                "Test where \"condition\" is false",
                expr!!
            ),
            ConditionChecklistNode(
                "Test where \"condition\" is true",
                expr!!
            )
        )
        val expectedNode = TestingChecklistMethodNode("twoBranchesNoParameter", expectedChildren, methodToGenerateOn)
        val actual = methodGenerator.generateChecklist(methodToGenerateOn)
        assertEquals(expectedNode, actual)
    }

    @Test
    fun testTryStatement() {
        getBasicTestInfo("/Person.java")

        val methodGenerator = MethodChecklistGenerationStrategy.create()
        val methodToGenerateOn = getMethodByName("getSpouseNameCatchAndFinally")

        val expr = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiTryStatement::class.java)
        val catch = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiCatchSection::class.java)
        val expectedChildren = mutableListOf<TestingChecklistLeafNode>(
            TryStatementChecklistNode(
                "Test with the try block running successfully",
                expr!!,
                null
            ),
            TryStatementChecklistNode(
                "Test with the try block throwing a NotMarriedException",
                catch!!,
                "NotMarriedException"
            ),
        )
        val expectedNode =
            TestingChecklistMethodNode("getSpouseNameCatchAndFinally", expectedChildren, methodToGenerateOn)
        val actual = methodGenerator.generateChecklist(methodToGenerateOn)
        assertEquals(expectedNode, actual)
    }

    @Test
    fun testSwitchStatement() {
        getBasicTestInfo("/Person.java")
        val methodGenerator = MethodChecklistGenerationStrategy.create()
        val methodToGenerateOn = getMethodByName("commentOnAge")

        val expr = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiSwitchStatement::class.java)
        val switchLabels = PsiTreeUtil.findChildrenOfType(methodToGenerateOn, PsiSwitchLabelStatement::class.java)

        val expectedChildren = mutableListOf<TestingChecklistLeafNode>(
            SwitchStatementChecklistNode(
                "Test this.age is 10",
                switchLabels.elementAt(0)!!,
                "this.age",
                "10"
            ),
            SwitchStatementChecklistNode(
                "Test this.age is 20",
                switchLabels.elementAt(1)!!,
                "this.age",
                "20"
            ),
            SwitchStatementChecklistNode(
                "Test this.age is 30",
                switchLabels.elementAt(2)!!,
                "this.age",
                "30"
            ),
            SwitchStatementChecklistNode(
                "Test this.age is 40",
                switchLabels.elementAt(3)!!,
                "this.age",
                "40"
            ),
            SwitchStatementChecklistNode(
                "Test this.age is different from all the switch cases",
                switchLabels.elementAt(4)!!,
                "this.age",
                null
            ),
        )
        val expectedNode = TestingChecklistMethodNode("commentOnAge", expectedChildren, methodToGenerateOn)
        val actual = methodGenerator.generateChecklist(methodToGenerateOn)
        assertEquals(expectedNode, actual)
    }

    @Test
    fun testDoWhileStatement() {
        getBasicTestInfo("/Person.java")
        val methodGenerator = MethodChecklistGenerationStrategy.create()
        val methodToGenerateOn = getMethodByName("doWhileExample")

        val expr = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiPrefixExpression::class.java)
        val doWhile = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiDoWhileStatement::class.java)
        val expectedChildren = mutableListOf<TestingChecklistLeafNode>(
            ConditionChecklistNode(
                "Test where \"!condition\" is false",
                expr!!
            ),
            ConditionChecklistNode(
                "Test where \"!condition\" is true",
                expr!!
            ),
            DoWhileStatementChecklistNode(
                "Test where do-while loop runs multiple times",
                doWhile!!
            )
        )
        val expectedNode = TestingChecklistMethodNode("doWhileExample", expectedChildren, methodToGenerateOn)
        val actual = methodGenerator.generateChecklist(methodToGenerateOn)
        assertEquals(expectedNode, actual)
    }

    @Test
    fun testForEachStatement() {
        getBasicTestInfo("/Person.java")

        val methodGenerator = MethodChecklistGenerationStrategy.create()
        val methodToGenerateOn = getMethodByName("spellWithForEach")

        val expr = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiForeachStatement::class.java)
        val expectedChildren = mutableListOf<TestingChecklistLeafNode>(
            ForEachStatementChecklistNode(
                "Test where this.name is empty",
                expr!!,
                "this.name"
            ),
            ForEachStatementChecklistNode(
                "Test where this.name has one element",
                expr!!,
                "this.name"
            ),
            ForEachStatementChecklistNode(
                "Test where this.name is null",
                expr!!,
                "this.name"
            ),
            ForEachStatementChecklistNode(
                "Test where foreach loop runs multiple times",
                expr!!,
                "this.name"
            )
        )
        val expectedNode = TestingChecklistMethodNode("spellWithForEach", expectedChildren, methodToGenerateOn)
        val actual = methodGenerator.generateChecklist(methodToGenerateOn)
        assertEquals(expectedNode, actual)
    }

    @Test
    fun testForStatement() {
        getBasicTestInfo("/Person.java")
        val methodGenerator = MethodChecklistGenerationStrategy.create()
        val methodToGenerateOn = getMethodByName("spellName")

        val expr = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiForStatement::class.java)
        val bin = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiBinaryExpression::class.java)
        val expectedChildren = mutableListOf<TestingChecklistLeafNode>(
            ConditionChecklistNode(
                "Test where \"i < this.name.length()\" is false",
                bin!!
            ),
            ConditionChecklistNode(
                "Test where \"i < this.name.length()\" is true",
                bin!!
            ),
            ForLoopStatementChecklistNode(
                "Test where for loop runs multiple times",
                expr!!
            )
        )
        val expectedNode = TestingChecklistMethodNode("spellName", expectedChildren, methodToGenerateOn)
        val actual = methodGenerator.generateChecklist(methodToGenerateOn)
        assertEquals(expectedNode, actual)
    }

    @Test
    fun testWhileStatement() {
        getBasicTestInfo("/Person.java")
        val methodGenerator = MethodChecklistGenerationStrategy.create()
        val methodToGenerateOn = getMethodByName("countToTen")

        val expr = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiBinaryExpression::class.java)
        val whileStatement = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiWhileStatement::class.java)
        val expectedChildren = mutableListOf<TestingChecklistLeafNode>(
            ConditionChecklistNode(
                "Test where \"counter < 11\" is false",
                expr!!
            ),
            ConditionChecklistNode(
                "Test where \"counter < 11\" is true",
                expr!!
            ),
            WhileStatementChecklistNode(
                "Test where while loop runs multiple times",
                whileStatement!!
            )
        )
        val expectedNode = TestingChecklistMethodNode("countToTen", expectedChildren, methodToGenerateOn)
        val actual = methodGenerator.generateChecklist(methodToGenerateOn)
        assertEquals(expectedNode, actual)
    }

    @Test
    fun testCombination() {
        getBasicTestInfo("/Person.java")
        // setFullName
        val methodGenerator = MethodChecklistGenerationStrategy.create()
        val methodToGenerateOn = getMethodByName("mysteriousMethodWithArray")

        val expr = methodToGenerateOn.parameterList.parameters[0]
        val expectedChildren = mutableListOf<TestingChecklistLeafNode>(
            ParameterChecklistNode(
                "Test method parameter \"a\" equal to: null",
                expr!!,
                "a",
                "null"
            ),
            ParameterChecklistNode(
                "Test method parameter \"a\" equal to: [1,2,3,4]",
                expr!!,
                "a",
                "[1,2,3,4]"
            ),
            ParameterChecklistNode(
                "Test method parameter \"a\" equal to: [4,3,2,1]",
                expr!!,
                "a",
                "[4,3,2,1]"
            ),
            ParameterChecklistNode(
                "Test method parameter \"a\" equal to: []",
                expr!!,
                "a",
                "[]"
            )
        )

        val expectedNode = TestingChecklistMethodNode("mysteriousMethodWithArray", expectedChildren, methodToGenerateOn)
        val actual = methodGenerator.generateChecklist(methodToGenerateOn)

        assertEquals(expectedNode, actual)
    }

    @Test
    fun testParameters() {
        getBasicTestInfo("/Person.java")
        // setFullName
        val methodGenerator = MethodChecklistGenerationStrategy.create()
        val methodToGenerateOn = getMethodByName("multipleStructs")

        val param = methodToGenerateOn.parameterList.parameters[0]
        val expr = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiPrefixExpression::class.java)
        val doWhile = PsiTreeUtil.findChildOfType(methodToGenerateOn, PsiDoWhileStatement::class.java)
        val expectedChildren = mutableListOf<TestingChecklistLeafNode>(
            ParameterChecklistNode(
                "Test method parameter \"a\" equal to: null",
                param!!,
                "a",
                "null"
            ),
            ParameterChecklistNode(
                "Test method parameter \"a\" equal to: [1,2,3,4]",
                param!!,
                "a",
                "[1,2,3,4]"
            ),
            ParameterChecklistNode(
                "Test method parameter \"a\" equal to: [4,3,2,1]",
                param!!,
                "a",
                "[4,3,2,1]"
            ),
            ParameterChecklistNode(
                "Test method parameter \"a\" equal to: []",
                param!!,
                "a",
                "[]"
            ),
            ConditionChecklistNode(
                "Test where \"!condition\" is false",
                expr!!
            ),
            ConditionChecklistNode(
                "Test where \"!condition\" is true",
                expr!!
            ),
            DoWhileStatementChecklistNode(
                "Test where do-while loop runs multiple times",
                doWhile!!
            )
        )
        val expectedNode = TestingChecklistMethodNode("multipleStructs", expectedChildren, methodToGenerateOn)
        val actual = methodGenerator.generateChecklist(methodToGenerateOn)
        assertEquals(expectedNode, actual)
    }
}
