package com.testknight.listeners

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.psi.PsiElement
import com.testknight.actions.LoadChecklistAction
import com.testknight.utilities.UserInterfaceHelper
import java.awt.event.MouseEvent

/**
 * Handler for the gutter icons which are used for the methods.
 */
class MethodChecklistIconHandler : GutterIconNavigationHandler<PsiElement> {

    /**
     * This handler generates the checklist for the chosen method.
     *
     * @param event  MouseEvent received from the click
     * @param element PsiElement for which you have to generate the checklist
     */
    override fun navigate(event: MouseEvent?, element: PsiElement?) {
        if (element == null) return
        val project = element.project
        if (LoadChecklistAction().actionPerformed(project, element.parent)) {
            UserInterfaceHelper.showTab(project, "Checklist")
        }
    }
}
