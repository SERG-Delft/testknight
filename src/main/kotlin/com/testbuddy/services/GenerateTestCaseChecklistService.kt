package com.testbuddy.com.testbuddy.services

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.ChecklistGenerator
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.IfStatementChecklistGenerator
import com.testbuddy.com.testbuddy.models.TestingChecklistItem
import org.junit.Test

class GenerateTestCaseChecklistService {


    private val ifStatementChecklistGenerator = IfStatementChecklistGenerator.create()

    fun generateChecklist(psiElement: PsiElement): List<TestingChecklistItem> {
        return when (psiElement) {
            is PsiIfStatement -> TODO()
            is PsiMethod -> TODO()
            is PsiParameter -> TODO()
            else -> emptyList<TestingChecklistItem>()
        }
    }

    /**
     * Generate the checklist for a given element.
     *
     * @param psiElement the element for which the element should be generated.
     * @param generator a function that takes a PsiElement and returns the list of checklist items. Note that the PsiElements must have
     */
    private fun generateChecklistForItem(
        psiElement: PsiElement,
        generator: (PsiElement) -> List<TestingChecklistItem>
    ): List<TestingChecklistItem> {
        return generator(psiElement);
    }


}