package com.testknight.checklistGenerationStrategies

import com.intellij.psi.JavaRecursiveElementVisitor
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassInitializer
import com.intellij.psi.PsiConditionalExpression
import com.intellij.psi.PsiDoWhileStatement
import com.intellij.psi.PsiForStatement
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameterList
import com.intellij.psi.PsiSwitchStatement
import com.intellij.psi.PsiThrowStatement
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.PsiWhileStatement
import com.intellij.psi.impl.source.PsiClassImpl
import com.testknight.checklistGenerationStrategies.leafStrategies.ParameterListChecklistGenerationStrategy
import com.testknight.checklistGenerationStrategies.leafStrategies.ThrowStatementChecklistGenerationStrategy
import com.testknight.checklistGenerationStrategies.leafStrategies.branchingStatements.ConditionalExpressionChecklistGenerationStrategy
import com.testknight.checklistGenerationStrategies.leafStrategies.branchingStatements.IfStatementChecklistGenerationStrategy
import com.testknight.checklistGenerationStrategies.leafStrategies.branchingStatements.SwitchStatementChecklistGenerationStrategy
import com.testknight.checklistGenerationStrategies.leafStrategies.branchingStatements.TryStatementChecklistGenerationStrategy
import com.testknight.checklistGenerationStrategies.leafStrategies.loopStatements.DoWhileStatementChecklistGenerationStrategy
import com.testknight.checklistGenerationStrategies.leafStrategies.loopStatements.ForEachStatementChecklistGenerationStrategy
import com.testknight.checklistGenerationStrategies.leafStrategies.loopStatements.ForStatementChecklistGenerationStrategy
import com.testknight.checklistGenerationStrategies.leafStrategies.loopStatements.WhileStatementChecklistGenerationStrategy
import com.testknight.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistClassNode
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import com.testknight.services.TestAnalyzerService
import com.testknight.settings.ChecklistSettingsValues
import com.testknight.settings.SettingsService

@Suppress("TooManyFunctions")
class TestKnightJavaPsiVisitor : JavaRecursiveElementVisitor() {

    private val enabledStrategies = SettingsService.instance.state.checklistSettings.checklistStrategies
    private val testAnalyzerService = TestAnalyzerService()

    var ifStatementChecklistGenerationStrategy = IfStatementChecklistGenerationStrategy.create()
    var conditionalExpressionChecklistGenerationStrategy = ConditionalExpressionChecklistGenerationStrategy.create()
    var switchStatementChecklistGenerationStrategy = SwitchStatementChecklistGenerationStrategy.create()
    var tryStatementChecklistGenerationStrategy = TryStatementChecklistGenerationStrategy.create()
    var parameterChecklistGenerationStrategy = ParameterListChecklistGenerationStrategy.create()
    var whileStatementChecklistGenerationStrategy = WhileStatementChecklistGenerationStrategy.create()
    var forStatementChecklistGenerationStrategy = ForStatementChecklistGenerationStrategy.create()
    var doWhileStatementChecklistGenerationStrategy = DoWhileStatementChecklistGenerationStrategy.create()
    var throwStatementChecklistGenerationStrategy = ThrowStatementChecklistGenerationStrategy.create()
    var forEachStatementChecklistGenerationStrategy = ForEachStatementChecklistGenerationStrategy.create()

    var classNode: TestingChecklistClassNode? = null
    var methodNode: TestingChecklistMethodNode? = null
    private var isTestMethod = false

    override fun visitClass(aClass: PsiClass) {
        if (aClass is PsiClassImpl) {
            classNode = TestingChecklistClassNode(aClass.name!!, mutableListOf(), aClass)
        }
        super.visitClass(aClass)
    }

    override fun visitClassInitializer(initializer: PsiClassInitializer?) {
        super.visitClassInitializer(initializer)
    }

    override fun visitMethod(method: PsiMethod) {
        val methodNode = TestingChecklistMethodNode(method.name, mutableListOf(), method)
        this.methodNode = methodNode
        isTestMethod = testAnalyzerService.isTestMethod(method)
        classNode?.children?.add(methodNode)
        super.visitMethod(method)
    }

    override fun visitConditionalExpression(expression: PsiConditionalExpression) {
        if (enabledStrategies[ChecklistSettingsValues.TERNARY_OPERATOR.valueName] == true && !isTestMethod) {
            addChildrenToLastMethod(
                conditionalExpressionChecklistGenerationStrategy.generateChecklist(
                    expression
                )
            )
        }
        super.visitConditionalExpression(expression)
    }

    override fun visitIfStatement(statement: PsiIfStatement) {
        if (enabledStrategies[ChecklistSettingsValues.IF_STATEMENT.valueName] == true && !isTestMethod) {
            addChildrenToLastMethod(ifStatementChecklistGenerationStrategy.generateChecklist(statement))
        }
        super.visitIfStatement(statement)
    }

    override fun visitSwitchStatement(statement: PsiSwitchStatement) {
        if (enabledStrategies[ChecklistSettingsValues.SWITCH_STATEMENT.valueName] == true && !isTestMethod) {
            addChildrenToLastMethod(switchStatementChecklistGenerationStrategy.generateChecklist(statement))
        }
        super.visitSwitchStatement(statement)
    }

    override fun visitTryStatement(statement: PsiTryStatement) {
        if (enabledStrategies[ChecklistSettingsValues.TRY_STATEMENT.valueName] == true && !isTestMethod) {
            addChildrenToLastMethod(tryStatementChecklistGenerationStrategy.generateChecklist(statement))
        }
        super.visitTryStatement(statement)
    }

    override fun visitDoWhileStatement(statement: PsiDoWhileStatement) {
        if (enabledStrategies[ChecklistSettingsValues.DO_WHILE_STATEMENT.valueName] == true && !isTestMethod) {
            addChildrenToLastMethod(doWhileStatementChecklistGenerationStrategy.generateChecklist(statement))
        }
        super.visitDoWhileStatement(statement)
    }

    override fun visitForeachStatement(statement: PsiForeachStatement) {
        if (enabledStrategies[ChecklistSettingsValues.FOREACH_STATEMENT.valueName] == true && !isTestMethod) {
            addChildrenToLastMethod(forEachStatementChecklistGenerationStrategy.generateChecklist(statement))
        }
        super.visitForeachStatement(statement)
    }

    override fun visitForStatement(statement: PsiForStatement) {
        if (enabledStrategies[ChecklistSettingsValues.FOR_STATEMENT.valueName] == true && !isTestMethod) {
            addChildrenToLastMethod(forStatementChecklistGenerationStrategy.generateChecklist(statement))
        }
        super.visitForStatement(statement)
    }

    override fun visitWhileStatement(statement: PsiWhileStatement) {
        if (enabledStrategies[ChecklistSettingsValues.WHILE_STATEMENT.valueName] == true && !isTestMethod) {
            addChildrenToLastMethod(whileStatementChecklistGenerationStrategy.generateChecklist(statement))
        }
        super.visitWhileStatement(statement)
    }

    override fun visitParameterList(list: PsiParameterList) {
        if (enabledStrategies[ChecklistSettingsValues.PARAMETER_LIST.valueName] == true && !isTestMethod) {
            addChildrenToLastMethod(parameterChecklistGenerationStrategy.generateChecklist(list))
        }
        super.visitParameterList(list)
    }

    override fun visitThrowStatement(statement: PsiThrowStatement) {
        if (enabledStrategies[ChecklistSettingsValues.THROW_STATEMENT.valueName] == true && !isTestMethod) {
            addChildrenToLastMethod(throwStatementChecklistGenerationStrategy.generateChecklist(statement))
        }
        super.visitThrowStatement(statement)
    }

    private fun addChildrenToLastMethod(leaves: Collection<TestingChecklistLeafNode>) {
        this.methodNode?.children?.addAll(leaves)
    }
}
