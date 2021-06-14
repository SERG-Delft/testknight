package com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiParameterList
import com.testbuddy.messageBundleHandlers.TestingChecklistMessageBundleHandler
import com.testbuddy.models.testingChecklist.leafNodes.ParameterChecklistNode
import com.testbuddy.settings.SettingsService

class ParameterListChecklistGenerationStrategy private constructor(
    private val typeChecklistCaseMap: Map<String, List<String>>
) :
    LeafChecklistGeneratorStrategy<PsiParameterList> {

    companion object Factory {

        /**
         * Creates a new ParameterChecklistGenerationStrategy.
         *
         * @return a new ParameterChecklistGenerationStrategy object.
         */
        fun create(): ParameterListChecklistGenerationStrategy {
            return ParameterListChecklistGenerationStrategy(getTypesCasesMap())
        }

        /**
         * Creates a new ParameterChecklistGenerationStrategy.
         *
         * @param typeChecklistCaseMap the map associating types to tests cases for them.
         * @return a new ParameterChecklistGenerationStrategy object.
         */
        fun create(typeChecklistCaseMap: Map<String, List<String>>): ParameterListChecklistGenerationStrategy {
            return ParameterListChecklistGenerationStrategy(typeChecklistCaseMap)
        }

        /**
         * Returns the mapping between types and test cases stored in the settings state.
         *
         * @return a map associating the types with their test cases.
         */
        private fun getTypesCasesMap() = ApplicationManager
            .getApplication()
            .getService(SettingsService::class.java)
            .state
            .checklistSettings
            .paramSuggestionMap
    }

    /**
     * Generates the checklist items for a parameter list.
     *
     * @param psiElement the parameter list
     * @return a list of TestingCheckListLeafNode objects corresponding to the checklist items.
     */
    override fun generateChecklist(psiElement: PsiParameterList): List<ParameterChecklistNode> {
        val result = mutableListOf<ParameterChecklistNode>()
        psiElement.parameters.forEach {
            result.addAll(generateChecklistForParameter(it))
        }
        return result
    }

    /**
     * Generates the checklist for a given parameter statement.
     *
     * @param psiElement the parameter statement.
     * @return a list of TestingChecklistLeafNode objects corresponding to the required checklist items.
     */
    fun generateChecklistForParameter(psiElement: PsiParameter): List<ParameterChecklistNode> {
        val typeOfParameter = psiElement.type.canonicalText
        val nameOfParameter = psiElement.name
        val casesForType = this.typeChecklistCaseMap[typeOfParameter] ?: emptyList()
        return casesForType.map {
            ParameterChecklistNode(
                TestingChecklistMessageBundleHandler.message(
                    "parameterBaseMessage",
                    nameOfParameter,
                    it
                ),
                psiElement,
                nameOfParameter,
                it
            )
        }
    }
}
