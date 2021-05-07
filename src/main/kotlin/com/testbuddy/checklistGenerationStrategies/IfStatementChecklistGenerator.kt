package com.testbuddy.com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIfStatement
import com.testbuddy.com.testbuddy.models.TestingChecklistItem

class IfStatementChecklistGenerator : ChecklistGenerator<PsiIfStatement> {

    companion object Factory {
        fun create() {
            TODO()
        }
    }

    override fun generateChecklist(psiElement: PsiIfStatement): List<TestingChecklistItem> {
        TODO("Not yet implemented")
    }

}