package com.testbuddy.services

import com.intellij.psi.PsiAssignmentExpression
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.models.sideEffectAnalysis.MethodCall
import com.testbuddy.models.sideEffectAnalysis.Assignment
import com.testbuddy.models.sideEffectAnalysis.Class
import com.testbuddy.models.sideEffectAnalysis.ClassFieldMutationSideEffect
import com.testbuddy.models.sideEffectAnalysis.Method
import com.testbuddy.models.sideEffectAnalysis.MethodCallOnClassFieldSideEffect
import com.testbuddy.models.sideEffectAnalysis.MethodCallOnParameterSideEffect
import com.testbuddy.models.sideEffectAnalysis.SideEffect

class MethodAnalyzerService {

    /**
     * Gets the side effects of a given method. It currently
     * recognises exceptions and class field mutations as side-effects.
     *
     * @param method the PsiMethod to be analyzed.
     * @return a list of SideEffect objects based on the side-effects the method has.
     */
    fun getSideEffects(method: PsiMethod): List<SideEffect> {
        val classFieldReassignmentSideEffects = getClassFieldsAffected(method)
        val argumentMutationSideEffects = getArgumentsAffected(method)
        return (classFieldReassignmentSideEffects + argumentMutationSideEffects).distinct()
    }

    // method calls for detecting argument mutations
    /**
     * Finds all the arguments affected by the method.
     *
     * @param method the method to analyze.
     * @return a list of MethodCallOnParameterSideEffect objects representing the found side-effects.
     */
    private fun getArgumentsAffected(method: PsiMethod): List<MethodCallOnParameterSideEffect> {
        val methodUnderAnalysis = Method.createFromMethod(method)
        val methodCalls = PsiTreeUtil
            .findChildrenOfType(method, PsiMethodCallExpression::class.java)
            .filter { methodFilter(it) }
            .map { MethodCall.create(it) }
        val parentClass = Class.createClassFromMethod(method)

        return methodCalls.flatMap {
            it.getMethodCallSideEffects<MethodCallOnParameterSideEffect>(
                methodUnderAnalysis.parameters,
                parentClass.fields,
                {
                    fieldName: String,
                    paramsInMethodScope: Map<String, String>,
                    ->
                    !parentClass.isClassField(fieldName, paramsInMethodScope)
                }
            ) { fieldName: String, methodName: String -> MethodCallOnParameterSideEffect(fieldName, methodName) }
        }
    }

    // methods for detecting class field mutations

    /**
     * Finds all the class fields affected by the method.
     *
     * @param method the method to be analyzed.
     * @return a list of MutatesClassFieldSideEffect objects
     * representing the class fields that change.
     */
    private fun getClassFieldsAffected(method: PsiMethod): List<ClassFieldMutationSideEffect> {
        val methodUnderAnalysis = Method.createFromMethod(method)
        val assignments =
            PsiTreeUtil.findChildrenOfType(method, PsiAssignmentExpression::class.java).map { Assignment.create(it) }
        val methodCalls =
            PsiTreeUtil.findChildrenOfType(method, PsiMethodCallExpression::class.java)
                .filter { methodFilter(it) }
                .map { MethodCall.create(it) }
        val parentClass = Class.createClassFromMethod(method)

        val classFieldsAffectedByAssignments = assignments.flatMap {
            it.getClassFieldsReassigned(
                methodUnderAnalysis.identifiersInScope,
                parentClass.fields
            )
        }

        val classFieldsAffectedByMethodCalls =
            methodCalls.flatMap {
                it.getMethodCallSideEffects<MethodCallOnClassFieldSideEffect>(
                    methodUnderAnalysis.identifiersInScope,
                    parentClass.fields,
                    {
                        fieldName: String,
                        paramsInMethodScope: Map<String, String>,
                        ->
                        parentClass.isClassField(fieldName, paramsInMethodScope)
                    }
                ) { fieldName: String, methodName: String -> MethodCallOnClassFieldSideEffect(fieldName, methodName) }
            }

        return classFieldsAffectedByAssignments + classFieldsAffectedByMethodCalls
    }

    private fun methodFilter(psiMethodCallExpression: PsiMethodCallExpression): Boolean {
        return !isChained(psiMethodCallExpression) && !isStatic(psiMethodCallExpression)
    }

    private fun isChained(psiMethodCallExpression: PsiMethodCallExpression): Boolean {
        val textualMethodCall = psiMethodCallExpression.text
        val charArray = textualMethodCall.toCharArray()
        var counter = 0
        for (i in 1 until charArray.size) {
            if (charArray[i] == ')' && charArray[i - 1] == '(') counter++
        }
        return counter > 1
    }

    private fun isStatic(psiMethodCallExpression: PsiMethodCallExpression): Boolean {
        return psiMethodCallExpression.text.matches(Regex("[A-Z]{1}.*"))
    }
}
