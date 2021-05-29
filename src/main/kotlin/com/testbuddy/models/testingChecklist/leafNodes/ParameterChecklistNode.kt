package com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiParameter

class ParameterChecklistNode(
    override var description: String,
    override val element: PsiParameter,
    val parameterName: String,
    val suggestedValue: String
) : TestingChecklistLeafNode(description, element) {
    override fun generateTestMethod(): PsiElement {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParameterChecklistNode

        if (description != other.description) return false
        if (element != other.element) return false
        if (parameterName != other.parameterName) return false
        if (suggestedValue != other.suggestedValue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = description.hashCode()
        result = 31 * result + element.hashCode()
        result = 31 * result + parameterName.hashCode()
        result = 31 * result + suggestedValue.hashCode()
        return result
    }


}