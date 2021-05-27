package com.testbuddy

import com.intellij.coverage.CoverageDataManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.impl.DocumentMarkupModel
import com.intellij.openapi.editor.impl.DocumentMarkupModelManager
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.models.TestCoverageData
import com.testbuddy.services.TestTracingService

// Temporary Test Action for debug purposes
class TestAction : AnAction() {

    @Suppress("UnusedPrivateMember")
    override fun actionPerformed(event: AnActionEvent) {
        val psiFile = event.getData(CommonDataKeys.PSI_FILE)!!
        val editor = event.getData(CommonDataKeys.EDITOR)!!
        val project = event.getData(CommonDataKeys.PROJECT)!!
        val caret = editor.caretModel.primaryCaret
        val elementAtCaret = psiFile.findElementAt(caret.offset)
        val parentMethod = PsiTreeUtil.getParentOfType(elementAtCaret, PsiMethod::class.java)
        val covDataManager = CoverageDataManager.getInstance(project)
        val markupModel = DocumentMarkupModel.forDocument(editor.document, project, true)
        val docMarkupModelManager = DocumentMarkupModelManager.getInstance(project)
        val fileEditorManager = FileEditorManager.getInstance(project)
        val covData = TestCoverageData("testo")
        covData.classes["Person"] = mutableListOf(1, 2, 3)

        val service = TestTracingService(project)
        service.activeCovData = covData

        service.refreshHighlights()
    }
}
