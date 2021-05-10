package com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements

import com.intellij.psi.PsiDoWhileStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.ChecklistGenerator
import com.testbuddy.com.testbuddy.models.TestingChecklistLeaf

class DoWhileStatementChecklistGenerationStrategy :
    ChecklistGenerator<PsiDoWhileStatement, TestingChecklistLeaf> {

    companion object Factory {
        fun create(): DoWhileStatementChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiDoWhileStatement): List<TestingChecklistLeaf> {
        TODO("Not yet implemented")
    }
}
