package com.testbuddy.models.sideEffectAnalysis

import com.testbuddy.messageBundleHandlers.AssertionSuggestionMessageBundleHandler
import com.testbuddy.models.AssertionSuggestion

abstract class SideEffect(open val info: String) {
    abstract fun toAssertionSuggestion(resolvedParameterName: String): AssertionSuggestion
}

abstract class ClassFieldMutationSideEffect(override val info: String) : SideEffect(info)
data class ReassignsClassFieldSideEffect(val nameOfField: String) : ClassFieldMutationSideEffect(nameOfField) {
    override fun toAssertionSuggestion(resolvedParameterName: String): AssertionSuggestion {
        return AssertionSuggestion(
            AssertionSuggestionMessageBundleHandler.message(
                "fieldReassignment",
                resolvedParameterName
            )
        )
    }
}

data class MethodCallOnClassFieldSideEffect(val nameOfField: String, val nameOfMethod: String) :
    ClassFieldMutationSideEffect(nameOfField) {
    override fun toAssertionSuggestion(resolvedParameterName: String): AssertionSuggestion {
        return AssertionSuggestion(
            AssertionSuggestionMessageBundleHandler
                .message(
                    "methodFieldModification",
                    nameOfMethod, resolvedParameterName
                )
        )
    }
}

data class MethodCallOnParameterSideEffect(val nameOfParameter: String, val nameOfMethod: String) :
    SideEffect(nameOfParameter) {
    override fun toAssertionSuggestion(resolvedParameterName: String): AssertionSuggestion {
        return AssertionSuggestion(
            AssertionSuggestionMessageBundleHandler
                .message(
                    "methodParameterModification",
                    nameOfMethod, resolvedParameterName
                )
        )
    }
}
