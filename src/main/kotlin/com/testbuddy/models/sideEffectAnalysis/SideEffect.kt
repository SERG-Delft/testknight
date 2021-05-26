package com.testbuddy.models.sideEffectAnalysis

import com.testbuddy.messageBundleHandlers.AssertionSuggestionMessageBundleHandler
import com.testbuddy.models.AssertionSuggestion

open class SideEffect(open val info: String)

abstract class ClassFieldMutationSideEffect(override val info: String) : SideEffect(info) {
    abstract fun toAssertionSuggestion(): AssertionSuggestion
}
data class ReassignsClassFieldSideEffect(val nameOfField: String) : ClassFieldMutationSideEffect(nameOfField) {
    override fun toAssertionSuggestion(): AssertionSuggestion {
        return AssertionSuggestion(
            AssertionSuggestionMessageBundleHandler.message(
                "fieldReassignment",
                nameOfField
            )
        )
    }
}

data class MethodCallOnClassFieldSideEffect(val nameOfField: String, val nameOfMethod: String) :
    ClassFieldMutationSideEffect(nameOfField) {
    override fun toAssertionSuggestion(): AssertionSuggestion {
        return AssertionSuggestion(
            AssertionSuggestionMessageBundleHandler
                .message(
                    "methodFieldModification",
                    nameOfMethod, nameOfField
                )
        )
    }
}

data class MethodCallOnParameterSideEffect(val nameOfParameter: String, val nameOfMethod: String) :
    SideEffect(nameOfParameter) {
    fun toAssertionSuggestion(resolvedParameterName: String): AssertionSuggestion {
        return AssertionSuggestion(
            AssertionSuggestionMessageBundleHandler
                .message(
                    "methodParameterModification",
                    nameOfMethod, resolvedParameterName
                )
        )
    }
}
