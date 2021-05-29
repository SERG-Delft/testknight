package com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.branchingStatements

import com.intellij.psi.PsiElement
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode

class SwitchStatementChecklistNode(
    override var description: String,
    override val element: PsiElement,
    val switchVariable: String,
    val value: String?,
    val isDefaultCase: Boolean = value == null
) : TestingChecklistLeafNode(description, element) {
    override fun generateTestMethod(): PsiElement {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SwitchStatementChecklistNode

        if (description != other.description) return false
        if (element != other.element) return false
        if (switchVariable != other.switchVariable) return false
        if (value != other.value) return false
        if (isDefaultCase != other.isDefaultCase) return false

        return true
    }

    override fun hashCode(): Int {
        var result = description.hashCode()
        result = 31 * result + element.hashCode()
        result = 31 * result + switchVariable.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + isDefaultCase.hashCode()
        return result
    }


}
