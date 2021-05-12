package com.testbuddy.com.testbuddy.services

import com.intellij.psi.PsiDoWhileStatement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiForStatement
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiSwitchStatement
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.PsiWhileStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.IfStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.SwitchStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.TryStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.ParameterChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements.DoWhileStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements.ForStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements.WhileStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies.ClassChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies.MethodChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistNode

class GenerateTestCaseChecklistService {

    var classChecklistGenerationStrategy = ClassChecklistGenerationStrategy.create()
    var methodChecklistGenerationStrategy = MethodChecklistGenerationStrategy.create()
    var ifStatementChecklistGenerationStrategy = IfStatementChecklistGenerationStrategy.create()
    var switchStatementChecklistGenerationStrategy = SwitchStatementChecklistGenerationStrategy.create()
    var tryStatementChecklistGenerationStrategy = TryStatementChecklistGenerationStrategy.create()
    var parameterChecklistGenerationStrategy = ParameterChecklistGenerationStrategy.create()
    var whileStatementChecklistGenerationStrategy = WhileStatementChecklistGenerationStrategy.create()
    var forStatementChecklistGenerationStrategy = ForStatementChecklistGenerationStrategy.create()
    var doWhileStatementChecklistGenerationStrategy = DoWhileStatementChecklistGenerationStrategy.create()
    // TODO ForEachStatement
    // TODO ThrowStatement

//    fun generateChecklist(file: PsiFile, editor: Editor): TestingChecklist {
//        val caret = editor.caretModel.primaryCaret
//        val offset = caret.offset
//        val element = file.findElementAt(offset)
//        val containingMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java)
//
//        return if (containingMethod != null) {
//            generateChecklist(containingMethod)
//            TODO()
//        } else { TODO() } // empty checklist }
//    }

    /**
     * Generates the testing checklist for a given PsiElement.
     *
     * @param psiElement the PsiElement to generate the checklist on.
     * @return the list of TestingChecklistItem objects representing the testing checklist item.
     */
    fun generateChecklist(psiElement: PsiElement): List<TestingChecklistNode> {
        return when (psiElement) {
            is PsiIfStatement -> ifStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiSwitchStatement -> switchStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiTryStatement -> tryStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiParameter -> parameterChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiWhileStatement -> whileStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiForStatement -> forStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            is PsiDoWhileStatement -> doWhileStatementChecklistGenerationStrategy.generateChecklist(psiElement)
            // TODO ForEachStatement
            // TODO ThrowStatement
            else -> emptyList<TestingChecklistNode>()
        }
    }
}
