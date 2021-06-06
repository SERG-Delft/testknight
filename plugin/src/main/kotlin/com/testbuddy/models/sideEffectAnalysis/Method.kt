package com.testbuddy.models.sideEffectAnalysis

import com.intellij.psi.PsiDeclarationStatement
import com.intellij.psi.PsiLocalVariable
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil

data class Method(
    val parameters: Map<String, String>,
    val localVariables: Map<String, String>,
    val identifiersInScope: Map<String, String>
) {

    companion object Factory {
        /**
         * Creates a new Method object from a PsiMethod.
         *
         * @param psiMethod the PsiMethod to create from.
         * @return a new Method object.
         */
        fun createFromMethod(psiMethod: PsiMethod): Method {
            val parameters = getParametersOfMethod(psiMethod)
            val localVariables = getLocalVariablesOfMethod(psiMethod)
            val identifiersInScope = getIdentifiersInMethodScope(psiMethod)
            return Method(parameters, localVariables, identifiersInScope)
        }

        /**
         * Gets the name of all the identifiers that are defined in the scope of the method.
         *
         * @param method the PsiMethod to analyze.
         * @return a set containing the names of the identifiers.
         */
        private fun getIdentifiersInMethodScope(method: PsiMethod): Map<String, String> {
            val localVariablesOfMethod = getLocalVariablesOfMethod(method)
            val parametersOfMethod = getParametersOfMethod(method)
            val result = mutableMapOf<String, String>()
            localVariablesOfMethod.forEach { result[it.key] = it.value }
            parametersOfMethod.forEach { result[it.key] = it.value }
            return result
        }

        private fun getParametersOfMethod(method: PsiMethod): Map<String, String> {
            val result = mutableMapOf<String, String>()
            val parameters = method.parameterList.parameters
            for (parameter in parameters) {
                result[parameter.name] = parameter.type.canonicalText
            }
            return result
        }

        private fun getLocalVariablesOfMethod(method: PsiMethod): Map<String, String> {
            val result = mutableMapOf<String, String>()
            val declarations = PsiTreeUtil.findChildrenOfType(method, PsiDeclarationStatement::class.java)
                .mapNotNull { PsiTreeUtil.getChildOfType(it, PsiLocalVariable::class.java) }
            for (declaration in declarations) {
                result[declaration.name] = declaration.type.canonicalText
            }
            return result
        }
    }
}
