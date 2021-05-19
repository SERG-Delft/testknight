package com.testbuddy.com.testbuddy.views.listeners

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import com.intellij.ui.CheckedTreeNode
import com.testbuddy.models.ChecklistUserObject
import com.testbuddy.models.TestingChecklistClassNode
import com.testbuddy.models.TestingChecklistLeafNode
import com.testbuddy.models.TestingChecklistMethodNode
import com.testbuddy.models.TestingChecklistNode
import javax.swing.event.TreeSelectionEvent
import javax.swing.event.TreeSelectionListener

class ChecklistSelectionListener(val project: Project) : TreeSelectionListener {

    private var editor: Editor = (FileEditorManager.getInstance(project).selectedEditor as TextEditor?)!!.editor
    private var highlighterList: MutableList<RangeHighlighter> = mutableListOf()

    override fun valueChanged(event: TreeSelectionEvent?) {
        event ?: return

        for (highlighter in highlighterList) {
            editor.markupModel.removeHighlighter(highlighter)
        }

        val path = event.newLeadSelectionPath
//        for (path in event.paths) {
        val component = path.lastPathComponent
        if (component is CheckedTreeNode) {
            val userObject = (component.userObject as ChecklistUserObject).checklistNode

            val element = getElement(userObject) ?: return

            if (isElementInEditor(editor, element)) {

                val myKey = DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT_HIGHLIGHTED

                highlighterList.add(
                    editor.markupModel.addRangeHighlighter(
                        myKey,
                        element.startOffset,
                        element.endOffset,
                        HighlighterLayer.SELECTION - 200,
                        HighlighterTargetArea.EXACT_RANGE
                    )
                )
            }
        }
//        }
    }

    private fun getElement(obj: TestingChecklistNode): PsiElement? {
        when (obj) {
            is TestingChecklistClassNode -> return obj.element
            is TestingChecklistMethodNode -> return obj.element
            is TestingChecklistLeafNode -> return obj.element
        }

        return null
    }

    private fun isElementInEditor(editor: Editor, psiElement: PsiElement?): Boolean {
        if (psiElement == null) return false
        val elementFile = psiElement.containingFile.virtualFile ?: return false
        val editorFile = FileDocumentManager.getInstance().getFile(editor.document)
        return elementFile == editorFile
    }
}
