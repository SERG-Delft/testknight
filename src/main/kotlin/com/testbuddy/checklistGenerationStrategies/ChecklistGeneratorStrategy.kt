package com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiElement

interface ChecklistGeneratorStrategy<E : PsiElement, G> {

    /**
     * Generates the checklist of all tests that should be created for a given PsiElement.
     *
     * @param psiElement the PsiElement for which the checklist should be generated.
     * @return a list of TestingChecklistItem objects corresponding to suggested test cases.
     */
    fun generateChecklist(psiElement: E): G
}
