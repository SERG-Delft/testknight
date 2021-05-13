package com.testbuddy.services

import com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies.ClassChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies.MethodChecklistGenerationStrategy

class GenerateTestCaseChecklistService {

    var classChecklistGenerationStrategy = ClassChecklistGenerationStrategy.create()
    var methodChecklistGenerationStrategy = MethodChecklistGenerationStrategy.create()

//    fun generateChecklist(file: PsiFile, editor: Editor): TestingChecklist {
//        val caret = editor.caretModel.primaryCaret
//        val offset = caret.offset
//        val element = file.findElementAt(offset)
//        val containingMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java)
//
//        return if (containingMethod != null) {
//            generateChecklist(containingMethod)
//            TODO()
//        } else { TODO() } // empty checklist }
//    }
}
