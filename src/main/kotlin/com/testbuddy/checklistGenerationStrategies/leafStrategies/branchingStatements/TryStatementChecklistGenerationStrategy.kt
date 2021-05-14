package com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements

import com.intellij.psi.PsiCatchSection
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class TryStatementChecklistGenerationStrategy private constructor() :
    LeafChecklistGeneratorStrategy<PsiTryStatement> {

    companion object Factory {
        fun create(): TryStatementChecklistGenerationStrategy {
            return TryStatementChecklistGenerationStrategy()
        }
    }

    override fun generateChecklist(psiElement: PsiTryStatement): List<TestingChecklistLeafNode> {
//        "Test with the try block running successfully"
//"Test with the try block throwing an exception"
        //for each PsiCatchSection extract the name of the exception
        val result = mutableListOf<TestingChecklistLeafNode>(
            TestingChecklistLeafNode(
                "Test with the try block running successfully",
                psiElement
            )
        )
        val catches = PsiTreeUtil.findChildrenOfType(psiElement, PsiCatchSection::class.java)
        if (catches.isEmpty()) return result
        catches.forEach {
            if (it.catchType != null) result.add(
                TestingChecklistLeafNode(
                    "Test with the try block throwing a ${it.catchType!!.canonicalText}",
                    it
                )
            )
        }
        return result
    }
}
