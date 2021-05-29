package com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiExpression
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.ConditionChecklistNode
import com.testbuddy.exceptions.InvalidConfigurationException
import com.testbuddy.messageBundleHandlers.TestingChecklistMessageBundleHandler
import com.testbuddy.models.PropositionalExpression
import com.testbuddy.models.TruthTable
import kotlin.math.pow

class ConditionChecklistGenerationStrategy private constructor(
    private val coverageGenerationMethod: ConditionCoverageType
) : LeafChecklistGeneratorStrategy<PsiExpression> {

    private enum class ConditionCoverageType {
        MCDC, BRANCH
    }

    companion object Factory {
        /**
         * Creates a new ConditionChecklistGenerationStrategy
         * with MC/DC coverage as the condition coverage type.
         *
         * @return a ConditionChecklistGenerationStrategy object.
         */
        fun createWithMcDcConditionCoverage(): ConditionChecklistGenerationStrategy {
            return ConditionChecklistGenerationStrategy(ConditionCoverageType.MCDC)
        }

        /**
         * Creates a new ConditionChecklistGenerationStrategy
         * with branch coverage as the condition coverage type.
         *
         * @return a ConditionChecklistGenerationStrategy object.
         */
        fun createWithBranchConditionCoverage(): ConditionChecklistGenerationStrategy {
            return ConditionChecklistGenerationStrategy(ConditionCoverageType.BRANCH)
        }

        /**
         * Creates a new ConditionChecklistGenerationStrategy
         * from the name of the the condition coverage type defined.
         *
         * @param conditionCoverageType the type of condition coverage. The only ones accepted are
         * "BRANCH" and "MCDC". Anything else will result to a InvalidConfigurationException.
         * @return a ConditionChecklistGenerationStrategy object.
         */
        fun createFromString(
            conditionCoverageType: String
        ): ConditionChecklistGenerationStrategy {
            val validTypes = ConditionCoverageType.values().map { it.name }.toSet()
            return if (validTypes.contains(conditionCoverageType)) {
                when (ConditionCoverageType.valueOf(conditionCoverageType)) {
                    ConditionCoverageType.MCDC -> createWithMcDcConditionCoverage()
                    ConditionCoverageType.BRANCH -> createWithBranchConditionCoverage()
                }
            } else {
                throw InvalidConfigurationException("condition coverage type", conditionCoverageType)
            }
        }
    }

    /**
     * Represents a test case in the form of a mapping between propositions and truth values
     *
     * @param bindings represents the bindings of each proposition to a truth value.
     */
    inner class TestCaseBindings(val bindings: Map<String, Boolean>) {

        /**
         * Converts the bindings to a string representation given assignments.
         *
         * @param assignments a map from string to string representing the actual names of each proposition.
         */
        fun getDescription(assignments: Map<String, String>): String {

            val description = StringBuilder("Test where ")

            bindings.entries.forEach { (proposition, value) ->
                description.append(
                    TestingChecklistMessageBundleHandler.message(
                        "conditionAssignmentMessage",
                        assignments[proposition]!!,
                        value
                    )
                )
            }

            return description.dropLast(2).toString()
        }
    }

    /**
     * Generates the testing checklist based on the configured coverage method.
     *
     * @param psiElement the PsiExpression to generate on.
     * @return a list of TestingChecklistLeafNodes that represent the checklist.
     */
    override fun generateChecklist(psiElement: PsiExpression): List<ConditionChecklistNode> {

        val (simplified, assignments) = PropositionalExpression(psiElement).simplified()

        return when (coverageGenerationMethod) {
            ConditionCoverageType.MCDC -> mcdc(assignments.keys.toList(), simplified)
                .map { ConditionChecklistNode(it.getDescription(assignments), psiElement) }

            ConditionCoverageType.BRANCH -> branchCoverage(simplified)
                .map { ConditionChecklistNode(it.getDescription(mapOf(simplified to psiElement.text)), psiElement) }
        }
    }

    /**
     * Computes all of the mc/dc test cases for an expression.
     *
     * @param variables a list of strings containing all of the propositional variables
     * @param expressionString the logical expression in string form
     * @return a set of String -> Boolean maps which assign values to each proposition in the test case
     */
    @Suppress("NestedBlockDepth")
    fun mcdc(variables: List<String>, expressionString: String): Set<TestCaseBindings> {

        val n = variables.size
        val truthTable = TruthTable(variables, expressionString)

        // set of truth table rows to include
        val rows = mutableSetOf<Int>()

        // for each variable, iterate over each independence pair
        for (varI in 0 until n) {

            val nGroups = 2.0.pow(varI).toInt()
            val groupSz = 2.0.pow(n - (varI + 1)).toInt()

            var done = false

            for (group in 0 until nGroups) {
                for (pair in 0 + (group * groupSz * 2) until groupSz + (group * groupSz * 2)) {

                    // get the values of the independence pair
                    val v1 = truthTable.value(pair)
                    val v2 = truthTable.value(pair + groupSz)

                    // if their values don't match add the test cases
                    if (v1 != v2 && !done) {
                        rows.add(pair)
                        rows.add(pair + groupSz)
                        done = true
                    }
                }
            }
        }

        return rows.map { TestCaseBindings(truthTable.assignments(it)) }.toSet()
    }

    /**
     * Computes all the branch coverage test cases for a given
     * expression string.
     *
     * @param expressionString the logical expression in string form.
     * @return a set of String -> Boolean maps which assign values to each proposition in the test case.
     */
    private fun branchCoverage(expressionString: String): Set<TestCaseBindings> {
        return setOf(
            TestCaseBindings(mapOf(expressionString to true)),
            TestCaseBindings(mapOf(expressionString to false))
        )
    }
}
