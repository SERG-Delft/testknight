package com.testbuddy.com.testbuddy.services

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiMethod
import com.testbuddy.com.testbuddy.models.AssertionSuggestion
import com.testbuddy.com.testbuddy.utilities.StringFormatter

class AssertionSuggestionService {

    fun getAssertions(method: PsiMethod, project: Project): List<AssertionSuggestion> {
        TODO()
    }

    fun getAssertion(method: PsiMethod, project: Project) {
        TODO()
    }

    private fun getAssertionOnOutput(method: PsiMethod): List<AssertionSuggestion> {
        val methodType = method.returnType?.getCanonicalText() ?: return emptyList()
        val methodName = StringFormatter.
        return listOf(AssertionSuggestion("Assert that "))
    }
}
