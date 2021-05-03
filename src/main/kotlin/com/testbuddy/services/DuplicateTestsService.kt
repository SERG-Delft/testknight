package com.testbuddy.services

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil

class DuplicateTestsService {

    /**
     * Duplicates the test case method under the caret.
     *
     * @param file the PSI file.
     * @param editor represents the current instance of a text editor.
     * @param project the active project.
     */
    fun addToPsi(file: PsiFile, editor: Editor, project: Project) {

        val caret = editor.caretModel.primaryCaret
        val offset = caret.offset
        val element = file.findElementAt(offset)
        val containingMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java)
        println(containingMethod?.name)

        if (containingMethod != null) {
            WriteCommandAction.runWriteCommandAction(project) { containingMethod.parent.add(containingMethod) }
        }
    }
}
