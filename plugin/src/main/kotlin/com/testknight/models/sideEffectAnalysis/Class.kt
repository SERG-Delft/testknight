package com.testknight.models.sideEffectAnalysis

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil

data class Class(val fields: Map<String, String>) {

    companion object Factory {
        /**
         * Creates a new Class object, containing information regarding the
         * class and the class fields.
         *
         * @param method a PsiMethod from which the class information is extracted.
         * @return a new Class object.
         */
        fun createClassFromMethod(method: PsiMethod): Class {
            val parentClass =
                PsiTreeUtil.getParentOfType(method, PsiClass::class.java) ?: return Class(emptyMap())
            val result = mutableMapOf<String, String>()
            parentClass.allFields.forEach {
                result[it.name] = it.type.canonicalText
                result["this.${it.name}"] = it.type.canonicalText
            }
            result["this"] = parentClass.name ?: "!UNKNOWN"
            return Class(result)
        }
    }

    /**
     * Checks whether an identifier is a class field.
     *
     * @param identifier the name of the identifier.
     * @param identifiersInMethodScope the identifiers in the method scope.
     * @return true iff identifier is a class field.
     */
    fun isClassField(
        identifier: String,
        identifiersInMethodScope: Map<String, String>
    ): Boolean {
        return if (identifier.contains("this.")) {
            val newName = identifier.replaceFirst("this.", "")
            !identifiersInMethodScope.contains(newName) && this.fields.contains(newName)
        } else {
            !identifiersInMethodScope.contains(identifier) && this.fields.contains(identifier)
        }
    }
}
