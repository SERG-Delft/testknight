package com.testbuddy.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.services.AssertionSuggestionService
import com.testbuddy.services.TestAnalyzerService
import com.testbuddy.services.UsageDataService

/**
 * Implements an intention action to generate the assertions suggestions for the selected method call.
 */
class AssertionSuggestionsAction : PsiElementBaseIntentionAction(), IntentionAction {

    /**
     * If this action is applicable, then the "Generate assertion suggestions" text will be shown in the dropdown menu
     */
    override fun getText(): String {
        return "Generate assertion suggestions"
    }

    /**
     * Returns text for name of this family of intentions.
     *
     * @return the intention family name.
     */
    override fun getFamilyName(): String {
        return "AssertionSuggestions"
    }

    /**
     *
     * Check if this intention action is available for the selected code part.
     * If this is the case, a dropdown menu with the option "Generate assertion suggestions" will appear.
     *
     *
     * Note: this method must do its checks quickly and return.
     *
     * @param project Project which have to be edited.
     * @param editor  Editor of the project source.
     * @param element PsiElement which is selected.
     * @return `true` if this intention action is available for the selected code part, 'false' otherwise.
     */
    override fun isAvailable(project: Project, editor: Editor, element: PsiElement): Boolean {

        val parentMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java)
        val checkElement = PsiTreeUtil.getParentOfType(element, PsiMethodCallExpression::class.java)
        val testAnalyzerService = TestAnalyzerService()

        if (element is PsiIdentifier && checkElement != null && parentMethod != null) {
            if (testAnalyzerService.isTestMethod(parentMethod)) {
                return true
            }
        }
        return false
    }

    /**
     * By applying this method, the generated assertions suggestions will be integrated into the code editor.
     *
     * @param project Project which have to be edited.
     * @param editor  Editor of the project source.
     * @param element PsiElement which is selected.
     */
    override fun invoke(project: Project, editor: Editor, element: PsiElement) {

        val parentMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java)
        val checkElement = PsiTreeUtil.getParentOfType(element, PsiMethodCallExpression::class.java)

        val assertionsService = project.service<AssertionSuggestionService>()
        assertionsService.appendAssertionsAsComments(parentMethod!!, checkElement!!, project)
        UsageDataService.instance.recordSuggestAssertion()
    }

    /**
     * Indicate if this intention action should be invoked inside write action.
     *
     * @return `true` if this intention action should be invoked inside write action `false` otherwise
     */
    override fun startInWriteAction(): Boolean {
        return true
    }
}
