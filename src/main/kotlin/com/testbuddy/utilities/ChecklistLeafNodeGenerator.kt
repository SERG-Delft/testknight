package com.testbuddy.com.testbuddy.utilities

import com.intellij.psi.PsiDoWhileStatement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiForStatement
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiSwitchStatement
import com.intellij.psi.PsiThrowStatement
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.PsiWhileStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.IfStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.SwitchStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.TryStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.ParameterChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.ThrowStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements.DoWhileStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements.ForStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements.WhileStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode
import com.testbuddy.com.testbuddy.models.TestingChecklistNode

class ChecklistLeafNodeGenerator {

    var ifStatementChecklistGenerationStrategy = IfStatementChecklistGenerationStrategy.create()
    var switchStatementChecklistGenerationStrategy = SwitchStatementChecklistGenerationStrategy.create()
    var tryStatementChecklistGenerationStrategy = TryStatementChecklistGenerationStrategy.create()
    var parameterChecklistGenerationStrategy = ParameterChecklistGenerationStrategy.create()
    var whileStatementChecklistGenerationStrategy = WhileStatementChecklistGenerationStrategy.create()
    var forStatementChecklistGenerationStrategy = ForStatementChecklistGenerationStrategy.create()
    var doWhileStatementChecklistGenerationStrategy = DoWhileStatementChecklistGenerationStrategy.create()
    var throwStatementChecklistGenerationStrategy = ThrowStatementChecklistGenerationStrategy.create()
    // TODO ForEachStatement

    /**
     * Generates the testing checklist for a given PsiElement.
     *
     * @param psiElement the PsiElement to generate the checklist on.
     * @return the list of TestingChecklistItem objects representing the testing checklist item.
     */
    fun generateChecklist(psiElement: PsiElement): List<TestingChecklistLeafNode> {
        return when (psiElement) {
            is PsiIfStatement -> ifStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiSwitchStatement -> switchStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiTryStatement -> tryStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiParameter -> parameterChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiWhileStatement -> whileStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiForStatement -> forStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiDoWhileStatement -> doWhileStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            // TODO ForEachStatement
            is PsiThrowStatement -> throwStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            else -> emptyList<TestingChecklistLeafNode>()
        }
    }


}