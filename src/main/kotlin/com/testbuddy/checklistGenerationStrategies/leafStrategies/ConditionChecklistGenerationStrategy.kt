package com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiExpression
import com.testbuddy.exceptions.InvalidConfigurationException
import com.testbuddy.messageBundleHandlers.TestingChecklistMessageBundleHandler
import com.testbuddy.models.PropositionalExpression
import com.testbuddy.models.TestingChecklistLeafNode
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
     */
    inner class TestCase(val assignments: Map<String, Boolean>) {
        fun getDescription(): String = if (assignments.size == 1) {
                val expr = assignments.keys.first()
                "Test where $expr is ${assignments[expr]}"
        } else {

            var description = ""

            assignments.entries.forEach { (proposition, value) ->
                description += "$proposition is $value, "
            }

            for ((proposition, value) in assignments.entries) {
                description += TestingChecklistMessageBundleHandler.message(
                    "conditionAssignmentMessage",
                    assignments[proposition]!!,
                    value
                )
            }

            description.dropLast(2)
        }
    }

    /**
     * Generates the testing checklist based on the configured coverage method.
     *
     * @param psiElement the PsiExpression to generate on.
     * @return a list of TestingChecklistLeafNodes that represent the checklist.
     */
    override fun generateChecklist(psiElement: PsiExpression): List<TestingChecklistLeafNode> {
        return when (coverageGenerationMethod) {
            ConditionCoverageType.MCDC -> mcDcCoverageGeneration(psiElement)
            ConditionCoverageType.BRANCH -> branchCoverageGeneration(psiElement)
        }
    }

    /**
     * Generates the testing checklist based on branch coverage.
     *
     * @param psiElement the PsiExpression to generate on.
     * @return a list of TestingChecklistLeafNodes that represent the checklist.
     */
    private fun branchCoverageGeneration(psiElement: PsiExpression): List<TestingChecklistLeafNode> {
        val (simplified, assignments) = PropositionalExpression(psiElement).simplified()
        val testCases = branchCoverage(simplified)
        return testCaseFormatter(testCases, psiElement, assignments)
    }

    /**
     * Generates the testing checklist based on MC/DC coverage.
     *
     * @param psiElement the PsiExpression to generate on.
     * @return a list of TestingChecklistLeafNodes that represent the checklist.
     */
    private fun mcDcCoverageGeneration(psiElement: PsiExpression): List<TestingChecklistLeafNode> {
        val (simplified, assignments) = PropositionalExpression(psiElement).simplified()
        val testCases = mcdc(assignments.keys.toList(), simplified)
        return testCaseFormatter(testCases, psiElement, assignments)
    }

    /**
     * Formats the test case map generated by the generation method.
     *
     * @param testCases the test cases.
     * @param psiElement the PsiExpression.
     * @param assignments the map of assignments.
     */
    private fun testCaseFormatter(
        testCases: Set<TestCase>,
        psiElement: PsiExpression,
        assignments: Map<String, String>
    ): List<TestingChecklistLeafNode> {
        return testCases.map { testCase ->

            var description = if (testCase.assignments.size == 1) {
                TestingChecklistMessageBundleHandler.message("conditionSingleMessage", psiElement.text)
            } else {
                TestingChecklistMessageBundleHandler.message("conditionBaseMessage", psiElement.text)
            }

            for ((proposition, value) in testCase.assignments.entries) {
                description += TestingChecklistMessageBundleHandler.message(
                    "conditionAssignmentMessage",
                    assignments[proposition]!!,
                    value
                )
            }

            // remove trailing comma and space
            TestingChecklistLeafNode(description.dropLast(2), psiElement, 0)
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
    fun mcdc(variables: List<String>, expressionString: String): Set<TestCase> {

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

        return rows.map { TestCase(truthTable.assignments(it)) }.toSet()
    }

    /**
     * Computes all the branch coverage test cases for a given
     * expression string.
     *
     * @param expressionString the logical expression in string form.
     * @return a set of String -> Boolean maps which assign values to each proposition in the test case.
     */
    private fun branchCoverage(expressionString: String): Set<TestCase> {
        return setOf(
            TestCase(mapOf(expressionString to true)),
            TestCase(mapOf(expressionString to false))
        )
    }
}
