package com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiNewExpression
import com.intellij.psi.PsiThrowStatement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.startOffset
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class ThrowStatementChecklistGenerationStrategy : LeafChecklistGeneratorStrategy<PsiThrowStatement> {
    override fun generateChecklist(throwStatement: PsiThrowStatement): List<TestingChecklistLeafNode> {
        TODO()
    }
}