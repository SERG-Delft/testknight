package com.testbuddy.com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode

class TestMethodGenerationService {

    fun generateTestMethod(file: PsiFile, checklistItem: TestingChecklistLeafNode) {
        val classToAddTheMethodTo = PsiTreeUtil.findChildOfType(file, PsiClass::class.java)
        
    }

}