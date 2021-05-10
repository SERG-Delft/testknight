package com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements

import com.intellij.psi.PsiWhileStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.ChecklistGenerator
import com.testbuddy.com.testbuddy.models.TestingChecklistLeaf

class WhileStatementChecklistGenerationStrategy :
    ChecklistGenerator<PsiWhileStatement, TestingChecklistLeaf> {

    companion object Factory {

        fun create(): WhileStatementChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiWhileStatement): List<TestingChecklistLeaf> {
        TODO("Not yet implemented")
    }
}
