package com.testbuddy.com.testbuddy.services

import com.intellij.psi.PsiDoWhileStatement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiForStatement
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiSwitchStatement
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.PsiWhileStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.DoWhileStatementChecklistGenerator
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.ForStatementChecklistGenerator
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.IfStatementChecklistGenerator
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.MethodChecklistGenerator
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.ParameterChecklistGenerator
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.SwitchStatementChecklistGenerator
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.TryStatementChecklistGenerator
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.WhileStatementChecklistGenerator
import com.testbuddy.com.testbuddy.models.TestingChecklistItem

class GenerateTestCaseChecklistService {

    private val ifStatementChecklistGenerator = IfStatementChecklistGenerator.create()
    private val switchStatementChecklistGenerator = SwitchStatementChecklistGenerator.create()
    private val tryStatementChecklistGenerator = TryStatementChecklistGenerator.create()
    private val methodChecklistGenerator = MethodChecklistGenerator.create()
    private val parameterChecklistGenerator = ParameterChecklistGenerator.create()
    private val whileStatementChecklistGenerator = WhileStatementChecklistGenerator.create()
    private val forStatementChecklistGenerator = ForStatementChecklistGenerator.create()
    private val doWhileStatementChecklistGenerator = DoWhileStatementChecklistGenerator.create()

    fun generateChecklist(psiElement: PsiElement): List<TestingChecklistItem> {
        return when (psiElement) {
            is PsiIfStatement -> ifStatementChecklistGenerator.generateChecklist(psiElement)
            is PsiSwitchStatement -> switchStatementChecklistGenerator.generateChecklist(psiElement)
            is PsiTryStatement -> tryStatementChecklistGenerator.generateChecklist(psiElement)
            is PsiMethod -> methodChecklistGenerator.generateChecklist(psiElement)
            is PsiParameter -> parameterChecklistGenerator.generateChecklist(psiElement)
            is PsiWhileStatement -> whileStatementChecklistGenerator.generateChecklist(psiElement)
            is PsiForStatement -> forStatementChecklistGenerator.generateChecklist(psiElement)
            is PsiDoWhileStatement -> doWhileStatementChecklistGenerator.generateChecklist(psiElement)
            else -> emptyList<TestingChecklistItem>()
        }
    }
}
