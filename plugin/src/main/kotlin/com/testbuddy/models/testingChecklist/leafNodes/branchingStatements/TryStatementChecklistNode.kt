package com.testbuddy.models.testingChecklist.leafNodes.branchingStatements

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.util.xmlb.annotations.OptionTag
import com.testbuddy.messageBundleHandlers.TestMethodGenerationMessageBundleHandler
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testbuddy.utilities.PsiConverter

data class TryStatementChecklistNode(
    override var description: String = "",
    @OptionTag(converter = PsiConverter::class)
    override var element: PsiElement? = null,
    val exceptionName: String? = "",
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
