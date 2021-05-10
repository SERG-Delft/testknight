package com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements

import com.intellij.psi.PsiSwitchStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.ChecklistGenerator
import com.testbuddy.com.testbuddy.models.TestingChecklistLeaf

class SwitchStatementChecklistGenerationStrategy :
    ChecklistGenerator<PsiSwitchStatement, TestingChecklistLeaf> {

    companion object Factory {
        fun create(): SwitchStatementChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiSwitchStatement): List<TestingChecklistLeaf> {
        TODO("Not yet implemented")
    }
}
