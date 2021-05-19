package com.testbuddy.com.testbuddy.models.sideEffectAnalysis

import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiReferenceExpression

data class MethodCall(val name: String, val args: List<String>) {

    companion object Factory {
        fun create(psiMethodCallExpression: PsiMethodCallExpression): MethodCall {
            val methodName = psiMethodCallExpression.methodExpression.qualifiedName
            val arguments = mutableListOf<String>()
            if (methodName.contains(".")) {
                val argumentAppliedOn = methodName.subSequence(0, methodName.lastIndexOf(".")) as String
                arguments.add(argumentAppliedOn)
            }
            psiMethodCallExpression.argumentList.expressions.forEach {
                if (it is PsiReferenceExpression) arguments.add(it.text)
            }
            return MethodCall(methodName, arguments)
        }
    }

}