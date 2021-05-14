package com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements

import com.intellij.psi.PsiCodeBlock
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiSwitchLabelStatement
import com.intellij.psi.PsiSwitchLabeledRuleStatement
import com.intellij.psi.PsiSwitchStatement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.containers.mapSmart
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class SwitchStatementChecklistGenerationStrategy private constructor() :
    LeafChecklistGeneratorStrategy<PsiSwitchStatement> {

    companion object Factory {
        fun create(): SwitchStatementChecklistGenerationStrategy {
            return SwitchStatementChecklistGenerationStrategy()
        }
    }

    /**
     * Generates the checklist for a given switch statement.
     *
     * @param psiElement the switch statement.
     * @return a list of TestingChecklistLeafNode objects corresponding to the required checklist items.
     */
    override fun generateChecklist(psiElement: PsiSwitchStatement): List<TestingChecklistLeafNode> {
        val reference = PsiTreeUtil.getChildOfType(psiElement, PsiReferenceExpression::class.java) ?: return emptyList()
        val switchVariable = reference.canonicalText
        val codeBlock = PsiTreeUtil.getChildOfType(psiElement, PsiCodeBlock::class.java) ?: return emptyList()
        val switchLabels =
            PsiTreeUtil.getChildrenOfType(codeBlock, PsiSwitchLabelStatement::class.java) ?: return emptyList()

        val descriptions = mutableListOf<String>()
        switchLabels.forEach { if (it != null) descriptions += getCaseValue(it, switchVariable) }
        return descriptions.map { TestingChecklistLeafNode(it, psiElement) }
    }

    /**
     * Creates a textual description of the test cases derived from a "case" statement.
     * It also supports enhanced switch statements by flattening out their values.
     *
     * @param caseExpression the case statement.
     * @param switchVariable the variable of the switch statement.
     * @return a list of descriptions for testing checklist items.
     */
    private fun getCaseValue(caseExpression: PsiSwitchLabelStatement, switchVariable: String): List<String> {
        if (caseExpression.isDefaultCase) {
            return listOf("Test $switchVariable is different from all the switch cases")
        }
        val caseValues =
            PsiTreeUtil.findChildrenOfType(caseExpression, PsiLiteralExpression::class.java).mapNotNull { it.text }
        return caseValues.map { "Test $switchVariable is $it" }
    }
}
