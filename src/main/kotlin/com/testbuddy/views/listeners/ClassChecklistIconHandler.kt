package com.testbuddy.views.listeners

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.psi.PsiElement
import com.testbuddy.utilities.UserInterfaceHelper
import com.testbuddy.views.actions.LoadChecklistAction
import java.awt.event.MouseEvent

/**
 * Handler for the gutter icons which are used for the class.
 */
class ClassChecklistIconHandler : GutterIconNavigationHandler<PsiElement> {

    /**
     * This handler generates the checklist for the chosen class.
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
