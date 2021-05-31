package com.testbuddy.services

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ScrollType
import com.intellij.refactoring.suggested.startOffset
import com.testbuddy.models.TestMethodData

/**
 *  Centers user view to the specified method.
 *
 *  @param editor the current instance of the text editor.
 *  @param testMethodData custom object containing a reference to the PSI method where the user view should be centered.
 */
class GotoTestService {

    fun gotoMethod(editor: Editor, testMethodData: TestMethodData) {
        editor.caretModel.primaryCaret.moveToOffset(testMethodData.psiMethod.startOffset)
        editor.scrollingModel.scrollToCaret(ScrollType.CENTER)
    }
}
