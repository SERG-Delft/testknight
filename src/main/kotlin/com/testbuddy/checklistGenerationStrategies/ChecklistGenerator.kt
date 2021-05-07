package com.testbuddy.com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiMethod
import com.testbuddy.com.testbuddy.models.TestingChecklistItem

interface ChecklistGenerator<E : PsiElement> {

    /**
     * Generates the checklist of all tests that should be created for a given PsiElement.
     *
     * @param psiElement the PsiElement for which the checklist should be generated.
     * @return a list of TestingChecklistItem objects corresponding to suggested test cases.
     */
    fun generateChecklist(psiElement: E): List<TestingChecklistItem>
}