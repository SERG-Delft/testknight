package com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements

import com.intellij.psi.PsiCatchSection
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.exceptions.InvalidConfigurationException
import com.testbuddy.messageBundleHandlers.TestingChecklistMessageBundleHandler
import com.testbuddy.models.TestingChecklistLeafNode

class TryStatementChecklistGenerationStrategy private constructor(
    private val generationType: TryGenerationType
) :
    LeafChecklistGeneratorStrategy<PsiTryStatement> {

    /**
     * An enum class containing the available types of
     * configurations for the Try generation.
     * There are two valid configurations:
     * CasePerExceptionGeneration: Each type of exception caught
     * generates a checklist item.
     * BinaryCaseGeneration: Only two items generated,
     * one for successful running of the code in the "try" block
     * and one for running of the code that resulted in an exception.
     */
    private enum class TryGenerationType {
        CasePerExceptionGeneration, BinaryCaseGeneration
    }

    companion object Factory {

        private const val defaultTryGenerationType = "CasePerExceptionGeneration"

        /**
         * Creates a new TryStatementChecklistGenerationStrategy.
         *
         * @return a TryStatementChecklistGenerationStrategy object.
         */
        fun create(): TryStatementChecklistGenerationStrategy {
            return createFromString(getTryGenerationTypeConfiguration())
        }

        fun createWithBinaryCaseGeneration(): TryStatementChecklistGenerationStrategy {
            return TryStatementChecklistGenerationStrategy(TryGenerationType.BinaryCaseGeneration)
        }

        fun createWithCasePerExceptionGeneration(): TryStatementChecklistGenerationStrategy {
            return TryStatementChecklistGenerationStrategy(TryGenerationType.CasePerExceptionGeneration)
        }

        fun createFromString(tryGenerationType: String): TryStatementChecklistGenerationStrategy {
            val validTypes = TryGenerationType.values().map { it.name }.toSet()
            return if (validTypes.contains(tryGenerationType)) {
                when (TryGenerationType.valueOf(tryGenerationType)) {
                    TryGenerationType.BinaryCaseGeneration -> createWithBinaryCaseGeneration()
                    TryGenerationType.CasePerExceptionGeneration -> createWithCasePerExceptionGeneration()
                }
            } else {
                throw InvalidConfigurationException("try case generation type", tryGenerationType)
            }
        }

        /**
         * Returns the configured try generation type.
         *
         * @return a string representing the configured try generation type.
         */
        private fun getTryGenerationTypeConfiguration(): String {
            // This is currently a placeholder, when we add
            // configuration files the tryGenerationType
            // will be read from there.
            val tryGenerationType = defaultTryGenerationType
            return tryGenerationType
        }
    }

    /**
     * Generates the checklist for a given try statement.
     *
     * @param psiElement the try statement.
     * @return a list of TestingChecklistLeafNode objects corresponding to the required checklist items.
     */
    override fun generateChecklist(psiElement: PsiTryStatement): List<TestingChecklistLeafNode> {
        return when (this.generationType) {
            TryGenerationType.CasePerExceptionGeneration -> generateCasePerException(psiElement)
            TryGenerationType.BinaryCaseGeneration -> generateBinaryCase(psiElement)
        }
    }

    /**
     * Generates the checklist for a given try statement based on
     * the CasePerException generation type.
     *
     * @param psiElement the try statement.
     * @return a list of TestingChecklistLeafNode objects corresponding to the required checklist items.
     */
    private fun generateCasePerException(psiElement: PsiTryStatement): List<TestingChecklistLeafNode> {
        val result = mutableListOf<TestingChecklistLeafNode>(
            TestingChecklistLeafNode(
                TestingChecklistMessageBundleHandler.message("tryBlockSuccess"),
                psiElement
            )
        )
        val catches = PsiTreeUtil.findChildrenOfType(psiElement, PsiCatchSection::class.java)
        if (catches.isEmpty()) {
            result.add(
                TestingChecklistLeafNode(
                    TestingChecklistMessageBundleHandler.message("tryBlockGeneralException"),
                    psiElement
                )
            )
            return result
        }
        catches.forEach {
            if (it.catchType != null) result.add(
                TestingChecklistLeafNode(
                    TestingChecklistMessageBundleHandler.message(
                        "tryBlockThrowsSpecifException",
                        it.catchType!!.canonicalText
                    ),
                    it
                )
            )
        }
        return result
    }

    /**
     * Generates the checklist for a given try statement based on
     * the BinaryCase generation type.
     *
     * @param psiElement the try statement.
     * @return a list of TestingChecklistLeafNode objects corresponding to the required checklist items.
     */
    private fun generateBinaryCase(psiElement: PsiTryStatement): List<TestingChecklistLeafNode> {
        return listOf(
            TestingChecklistLeafNode(
                TestingChecklistMessageBundleHandler
                    .message("tryBlockSuccess"),
                psiElement
            ),
            TestingChecklistLeafNode(
                TestingChecklistMessageBundleHandler
                    .message("tryBlockGeneralException"),
                psiElement
            )
        )
    }
}
