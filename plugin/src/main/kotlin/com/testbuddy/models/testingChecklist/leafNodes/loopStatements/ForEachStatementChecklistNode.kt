package com.testbuddy.models.testingChecklist.leafNodes.loopStatements

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.util.xmlb.annotations.OptionTag
import com.testbuddy.messageBundleHandlers.TestMethodGenerationMessageBundleHandler
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testbuddy.utilities.PsiConverter

data class ForEachStatementChecklistNode(
    override var description: String = "",
    @OptionTag(converter = PsiConverter::class)
    override var element: PsiElement? = null,
    val iteratedValue: String = ""
) : TestingChecklistLeafNode(
    description, element
) {
    /**
     * Generate a test method for a for each statement.
     *
     * @param project the project.
     * @return the PsiMethod representing the test.
     */
    override fun generateTestMethod(project: Project): PsiMethod {
        val methodName = TestMethodGenerationMessageBundleHandler.message("forEachTestCaseName")
        return super.generateTestMethod(project, methodName)
    }
}
