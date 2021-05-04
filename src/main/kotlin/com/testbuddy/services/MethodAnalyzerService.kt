package com.testbuddy.com.testbuddy.services

import com.intellij.psi.PsiJavaCodeReferenceElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiThrowStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.models.ModifiesClassFieldSideEffect
import com.testbuddy.com.testbuddy.models.MutatesArgumentSideEffect
import com.testbuddy.com.testbuddy.models.SideEffect
import com.testbuddy.com.testbuddy.models.ThrowsExceptionSideEffect

class MethodAnalyzerService {

    fun getSideEffects(method: PsiMethod): List<SideEffect> {
        // If it has exception add exception thrown side-effect
        // If it affects class fields add ModifiesClassField side-effect
        // If it mutates the argument add mutates argument side-effect
        val exceptionSideEffects = getExceptionsThrown(method)
        val classFieldMutationSideEffects = listOf<ModifiesClassFieldSideEffect>()
        val argumentMutationSideEffects = listOf<MutatesArgumentSideEffect>()
        return exceptionSideEffects + classFieldMutationSideEffects + argumentMutationSideEffects
    }

    /**
     * Finds all the exceptions thrown by the given method.
     * If there are no exceptions thrown it returns an empty list.
     * If there are exception explicitly thrown from the body of the method it returns those.
     * If there is only a `throw` declaration in the signature it returns that.
     * If there is both a declaration in the signature and throws in the body, it returns
     * the exceptions of the body since those will be more specific.
     *
     * @param method the PsiMethod to be analyzed.
     * @return a list of ThrowsExceptionSideEffect based on the exceptions the method throws.
     */
    private fun getExceptionsThrown(method: PsiMethod): List<ThrowsExceptionSideEffect> {
        val throwStatements = PsiTreeUtil.findChildrenOfType(method.body, PsiThrowStatement::class.java)
        return if (throwStatements.isNotEmpty()) { // the body contains throws
            val result = mutableListOf<ThrowsExceptionSideEffect>()
            for (throwStatement in throwStatements) {
                val exception = PsiTreeUtil.findChildOfType(throwStatement, PsiJavaCodeReferenceElement::class.java)
                if (exception != null) {
                    result.add(ThrowsExceptionSideEffect(exception.qualifiedName))
                }
            }
            result
        } else if (throwStatements.isEmpty()) { // the signature contains a throw
            method.throwsList.referenceElements.map { ThrowsExceptionSideEffect(it.qualifiedName) }
        } else { // no exception found
            emptyList()
        }
    }
}
