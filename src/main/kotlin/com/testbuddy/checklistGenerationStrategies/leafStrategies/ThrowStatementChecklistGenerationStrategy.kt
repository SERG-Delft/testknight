package com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiThrowStatement
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class ThrowStatementChecklistGenerationStrategy : LeafChecklistGeneratorStrategy<PsiThrowStatement> {

    companion object Factory {

        fun create(): ThrowStatementChecklistGenerationStrategy {
            TODO()
        }

    }

    override fun generateChecklist(throwStatement: PsiThrowStatement): List<TestingChecklistLeafNode> {
        TODO()
    }
}
