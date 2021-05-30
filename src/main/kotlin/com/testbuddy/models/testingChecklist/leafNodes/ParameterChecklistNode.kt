package com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameter
import com.testbuddy.com.testbuddy.messageBundleHandlers.TestMethodGenerationMessageBundleHandler

data class ParameterChecklistNode(
    override var description: String,
    override val element: PsiParameter,
    val parameterName: String,
    val suggestedValue: String
) : TestingChecklistLeafNode(description, element) {
    /**
     * Generate a test method for a parameter.
     *
     * @param project the project.
     * @return the PsiMethod representing the test.
     */
    override fun generateTestMethod(project: Project): PsiMethod {
        val methodName = TestMethodGenerationMessageBundleHandler.message("parameterTestCaseName")
        return super.generateTestMethod(project, methodName)
    }
}
