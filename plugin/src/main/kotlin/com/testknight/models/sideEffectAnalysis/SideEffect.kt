package com.testknight.models.sideEffectAnalysis

import com.testknight.messageBundleHandlers.AssertionSuggestionMessageBundleHandler
import com.testknight.models.AssertionSuggestion

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
                    "classFieldModification",
                    nameOfMethod, nameOfField
                )
        )
    }
}

data class ReassignmentOfTransitiveField(val nameOfField: String, val nameOfTransitiveField: String) :
    ClassFieldMutationSideEffect(nameOfField) {
    override fun toAssertionSuggestion(): AssertionSuggestion {
        return AssertionSuggestion(
            AssertionSuggestionMessageBundleHandler.message(
                "transitiveFieldReassignment",
                nameOfTransitiveField, nameOfField
            )
        )
    }
}

abstract class ArgumentMutationSideEffect(open val nameOfParameter: String) : SideEffect(nameOfParameter) {
    abstract fun toAssertionSuggestion(resolvedParameterName: String): AssertionSuggestion
}

data class MethodCallOnParameterSideEffect(override val nameOfParameter: String, val nameOfMethod: String) :
    ArgumentMutationSideEffect(nameOfParameter) {
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

data class ParameterFieldReassignmentSideEffect(
    override val nameOfParameter: String,
    val nameOfField: String
) : ArgumentMutationSideEffect(nameOfParameter) {
    override fun toAssertionSuggestion(resolvedParameterName: String): AssertionSuggestion {
        return AssertionSuggestion(
            AssertionSuggestionMessageBundleHandler
                .message(
                    "parameterFieldReassignment",
                    nameOfField, resolvedParameterName
                )
        )
    }
}
