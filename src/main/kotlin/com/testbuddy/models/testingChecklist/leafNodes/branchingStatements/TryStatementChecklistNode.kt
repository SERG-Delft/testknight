package com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.branchingStatements

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.testbuddy.com.testbuddy.messageBundleHandlers.TestMethodGenerationMessageBundleHandler
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode

data class TryStatementChecklistNode(
    override var description: String,
    override val element: PsiElement,
    val exceptionName: String?,
    val isExceptionThrown: Boolean = exceptionName != null
) : TestingChecklistLeafNode(description, element) {

    /**
     * Generate a test method for a try statement.
     *
     * @param project the project.
     * @return the PsiMethod representing the test.
     */
    override fun generateTestMethod(project: Project): PsiMethod {
        val methodName = TestMethodGenerationMessageBundleHandler.message("tryTestCaseName")
        return super.generateTestMethod(project, methodName)
    }
}
