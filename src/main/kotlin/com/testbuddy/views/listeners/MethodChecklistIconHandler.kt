package com.testbuddy.views.listeners

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.psi.PsiElement
import com.testbuddy.views.actions.LoadChecklistAction
import java.awt.event.MouseEvent

class MethodChecklistIconHandler : GutterIconNavigationHandler<PsiElement> {

    override fun navigate(event: MouseEvent?, element: PsiElement?) {
        if (element == null) return
        val project = element.project
        LoadChecklistAction().actionPerformed(project, element.parent)
    }
}
