package com.testknight.models.testingChecklist.leafNodes

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.util.xmlb.annotations.OptionTag
import com.testknight.messageBundleHandlers.TestMethodGenerationMessageBundleHandler
import com.testknight.utilities.PsiConverter

data class ConditionChecklistNode(
    override var description: String = "",
    @OptionTag(converter = PsiConverter::class)
    override var element: PsiElement? = null
) : TestingChecklistLeafNode(description, element) {
    /**
     * Generate a test method for a  condition statement.
     *
     * @param project the project.
     * @return the PsiMethod representing the test.
     */
    override fun generateTestMethod(project: Project): PsiMethod {
        val methodName = TestMethodGenerationMessageBundleHandler.message("conditionTestCaseName")
        return super.generateTestMethod(project, methodName)
    }
}
