package com.testknight.listeners.checklist

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.testknight.actions.checklist.LoadChecklistAction
import com.testknight.utilities.UserInterfaceHelper
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
            project.service<UserInterfaceHelper>().showTab("Checklist")
        }
    }
}
