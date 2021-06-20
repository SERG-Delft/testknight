package com.testknight.models.testingChecklist.leafNodes.loopStatements

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.util.xmlb.annotations.OptionTag
import com.testknight.messageBundleHandlers.TestMethodGenerationMessageBundleHandler
import com.testknight.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testknight.utilities.PsiConverter

data class DoWhileStatementChecklistNode(
    override var description: String = "",
    @OptionTag(converter = PsiConverter::class)
    override var element: PsiElement? = null
) : TestingChecklistLeafNode(description, element) {
    override fun generateTestMethod(project: Project): PsiMethod {
        val methodName = TestMethodGenerationMessageBundleHandler.message("doWhileTestCaseName")
        return super.generateTestMethod(project, methodName)
    }
}
