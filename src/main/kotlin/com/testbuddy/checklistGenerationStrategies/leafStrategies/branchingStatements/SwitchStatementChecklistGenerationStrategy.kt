package com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements

import com.intellij.psi.PsiSwitchStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeaf

class SwitchStatementChecklistGenerationStrategy private constructor():
    LeafChecklistGeneratorStrategy<PsiSwitchStatement> {

    companion object Factory {
        fun create(): SwitchStatementChecklistGenerationStrategy {
            return SwitchStatementChecklistGenerationStrategy()
        }
    }

    override fun generateChecklist(psiElement: PsiSwitchStatement): List<TestingChecklistLeaf> {
        TODO()
    }
}
