package com.testbuddy.views.actions

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiMethod
import com.testbuddy.views.listeners.MethodChecklistIconHandler

class ChecklistMethodLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {

        if (element is PsiIdentifier && element.parent is PsiMethod) {
            return LineMarkerInfo(
                element, element.textRange, AllIcons.General.Add, null,
                MethodChecklistIconHandler(), GutterIconRenderer.Alignment.RIGHT
            )
        }
        return null
    }
}
