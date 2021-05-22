package com.testbuddy.views.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.psi.PsiClass
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.services.CoverageHighlighterService

class ShowDiffCovHighlights : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        val className = PsiTreeUtil.findChildOfType(psiFile, PsiClass::class.java)?.name ?: return

        val coverageHighlighterService = project.service<CoverageHighlighterService>()

        coverageHighlighterService.showHighlights(editor, className)
    }
}
