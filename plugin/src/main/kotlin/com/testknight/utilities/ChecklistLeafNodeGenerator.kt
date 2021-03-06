package com.testknight.utilities

import com.intellij.psi.PsiConditionalExpression
import com.intellij.psi.PsiDoWhileStatement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiForStatement
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiParameterList
import com.intellij.psi.PsiSwitchStatement
import com.intellij.psi.PsiThrowStatement
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.PsiWhileStatement
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

class ChecklistLeafNodeGenerator {

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

    /**
     * Generates the testing checklist for a given PsiElement.
     *
     * @param psiElement the PsiElement to generate the checklist on.
     * @return the list of TestingChecklistItem objects representing the testing checklist item.
     */
    fun generateChecklist(psiElement: PsiElement): List<TestingChecklistLeafNode> {
        return when (psiElement) {
            is PsiIfStatement -> ifStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiConditionalExpression ->
                conditionalExpressionChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiSwitchStatement -> switchStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiTryStatement -> tryStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiParameterList -> parameterChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiWhileStatement -> whileStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiForStatement -> forStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiDoWhileStatement -> doWhileStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiForeachStatement -> forEachStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiThrowStatement -> throwStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            else -> emptyList<TestingChecklistLeafNode>()
        }
    }
}
