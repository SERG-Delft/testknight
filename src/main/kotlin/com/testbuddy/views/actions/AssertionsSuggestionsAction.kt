package com.testbuddy.views.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.services.TestAnalyzerService

/**
 * Implements an intention action to generate the assertions suggestions for the selected method call.
 */
class AssertionsSuggestionsAction : PsiElementBaseIntentionAction(), IntentionAction {

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
        return "AssertionSuggestionIntention"
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
     * @return `true` f this intention action is available for the selected code part, 'false' otherwise.
     */
    @Suppress("ReturnCount")
    override fun isAvailable(project: Project, editor: Editor, element: PsiElement): Boolean {

        // println("isAvailable")

        if (element == null) {
            // println("kaiwkaiwk")
            return false
        }

        val parentMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java)
        val checkElement = PsiTreeUtil.getParentOfType(element, PsiMethodCallExpression::class.java)
        val testAnalyzerService = TestAnalyzerService()

        if (element is PsiIdentifier && checkElement != null && parentMethod != null) {
            if (testAnalyzerService.isTestMethod(parentMethod)) {
               // println("I have found a call")
                   // println(checkElement.methodExpression)
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

        println("invoke")

        // /Here the code from the assertions service
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
