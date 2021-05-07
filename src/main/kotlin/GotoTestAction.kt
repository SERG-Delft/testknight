package com.testbuddy

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.services.GotoTestService
import com.testbuddy.models.TestMethodData

class GotoTestAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val methodInstance = GotoTestService()
        val editor = event.getData(CommonDataKeys.EDITOR)
        val psi = event.getData(CommonDataKeys.PSI_FILE)

        if (editor != null) {
            val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
            if (testClass != null) {
                methodInstance.gotoMethod(
                    editor,
                    TestMethodData(
                        "parameterizedTest",
                        "PointTest", testClass.findMethodsByName("setYTest")[0] as PsiMethod
                    )
                )
            }
        }
    }
}
