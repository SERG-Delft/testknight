package com.testbuddy.com.testbuddy.services

import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiDoWhileStatement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiForStatement
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiSwitchStatement
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.PsiWhileStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.MethodChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.ParameterChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.IfStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.SwitchStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements.TryStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements.DoWhileStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements.ForStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements.WhileStatementChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklist
import com.testbuddy.com.testbuddy.models.TestingChecklistNode

class GenerateTestCaseChecklistService {

    private val ifStatementChecklistGenerator = IfStatementChecklistGenerationStrategy.create()
    private val switchStatementChecklistGenerator = SwitchStatementChecklistGenerationStrategy.create()
    private val tryStatementChecklistGenerator = TryStatementChecklistGenerationStrategy.create()
    private val methodChecklistGenerator = MethodChecklistGenerationStrategy.create()
    private val parameterChecklistGenerator = ParameterChecklistGenerationStrategy.create()
    private val whileStatementChecklistGenerator = WhileStatementChecklistGenerationStrategy.create()
    private val forStatementChecklistGenerator = ForStatementChecklistGenerationStrategy.create()
    private val doWhileStatementChecklistGenerator = DoWhileStatementChecklistGenerationStrategy.create()

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
            is PsiIfStatement -> ifStatementChecklistGenerator.generateChecklist(psiElement)
            is PsiSwitchStatement -> switchStatementChecklistGenerator.generateChecklist(psiElement)
            is PsiTryStatement -> tryStatementChecklistGenerator.generateChecklist(psiElement)
            is PsiMethod -> methodChecklistGenerator.generateChecklist(psiElement)
            is PsiParameter -> parameterChecklistGenerator.generateChecklist(psiElement)
            is PsiWhileStatement -> whileStatementChecklistGenerator.generateChecklist(psiElement)
            is PsiForStatement -> forStatementChecklistGenerator.generateChecklist(psiElement)
            is PsiDoWhileStatement -> doWhileStatementChecklistGenerator.generateChecklist(psiElement)
            else -> emptyList<TestingChecklistNode>()
        }
    }
}
