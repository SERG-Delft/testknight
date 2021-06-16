package com.testknight.models.sideEffectAnalysis

import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiReferenceExpression
import com.testknight.utilities.StringFormatter

data class MethodCall(val name: String, val args: List<String>) {

    private val primitives = setOf(
        "byte", "short", "int", "long",
        "float", "double", "char", "boolean",
        "!UNKNOWN"
    )

    companion object Factory {
        /**
         * Create a new MethodCall object from a PsiMethodCallExpression.
         *
         * @param psiMethodCallExpression the PsiMethodCallExpression to create the object from.
         * @return a new MethodCall object.
         */
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

    /**
     * A higher-order method that can be used to detect mutations caused by method calls.
     *
     * @param E the type of side-effect to be returned.
     * @param identifiersInClassScope the identifiers in the class scope.
     * @param identifiersInMethodScope the identifiers in the method scope.
     * @param fieldsFilter the function to be used to filter the arguments of the method call.
     * @param mapToResult the function to be used to create the final result.
     * @return a list of side-effects of type E.
     */
    fun <E : SideEffect> getMethodCallSideEffects(
        identifiersInMethodScope: Map<String, String>,
        identifiersInClassScope: Map<String, String>,
        fieldsFilter: (String, Map<String, String>) -> Boolean,
        mapToResult: (String, String) -> E,
    ): List<E> {
        val argumentsThatAreClassFields = this.args.filter {
            fieldsFilter(it, identifiersInMethodScope)
        }
        val argumentsThatAreReferences =
            argumentsThatAreClassFields.filter { !primitives.contains(identifiersInClassScope[it]) }
        return argumentsThatAreReferences.map {
            mapToResult(
                StringFormatter.formatClassFieldName(it),
                StringFormatter.formatMethodName(this.name)
            )
        }
    }
}
