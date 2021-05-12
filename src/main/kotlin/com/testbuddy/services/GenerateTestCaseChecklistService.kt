package com.testbuddy.com.testbuddy.services

import com.intellij.openapi.editor.Editor
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies.ClassChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies.MethodChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.ParameterChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.IfStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.SwitchStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.TryStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements.DoWhileStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements.ForStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements.WhileStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklist
import com.testbuddy.com.testbuddy.models.TestingChecklistNode

class GenerateTestCaseChecklistService {

    fun generateChecklist(file: PsiFile, editor: Editor): TestingChecklist {
        val caret = editor.caretModel.primaryCaret
        val offset = caret.offset
        val element = file.findElementAt(offset)
        val containingMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java)

        return if (containingMethod != null) {
            generateChecklist(containingMethod)
            TODO()
        } else { TODO() } // empty checklist }
    }

    /**
     * Generates the testing checklist for a given PsiElement.
     *
     * @param psiElement the PsiElement to generate the checklist on.
     * @return the list of TestingChecklistItem objects representing the testing checklist item.
     */
    fun generateChecklist(psiElement: PsiElement): List<TestingChecklistNode> {
        return when (psiElement) {
            is PsiClass -> ClassChecklistGenerationStrategy.create().generateChecklist(psiElement)
            is PsiIfStatement -> IfStatementChecklistGenerationStrategy.create().generateChecklist(psiElement)
            is PsiSwitchStatement -> SwitchStatementChecklistGenerationStrategy.create().generateChecklist(psiElement)
            is PsiTryStatement -> TryStatementChecklistGenerationStrategy.create().generateChecklist(psiElement)
            is PsiMethod -> MethodChecklistGenerationStrategy.create().generateChecklist(psiElement)
            is PsiParameter -> ParameterChecklistGenerationStrategy.create().generateChecklist(psiElement)
            is PsiWhileStatement -> WhileStatementChecklistGenerationStrategy.create().generateChecklist(psiElement)
            is PsiForStatement -> ForStatementChecklistGenerationStrategy.create().generateChecklist(psiElement)
            is PsiDoWhileStatement -> DoWhileStatementChecklistGenerationStrategy.create().generateChecklist(psiElement)
            else -> emptyList<TestingChecklistNode>()
        }
    }
}
