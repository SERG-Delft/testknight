package com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements

import com.intellij.psi.PsiForStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeaf

class ForStatementChecklistGenerationStrategy :
    LeafChecklistGeneratorStrategy<PsiForStatement> {

    companion object Factory {
        fun create(): ForStatementChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiForStatement): List<TestingChecklistLeaf> {
        TODO("Not yet implemented")
    }
}
