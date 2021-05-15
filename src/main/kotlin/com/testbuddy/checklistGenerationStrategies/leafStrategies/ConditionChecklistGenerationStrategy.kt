package com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiExpression
import com.testbuddy.com.testbuddy.models.PropositionalExpression
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode
import com.testbuddy.com.testbuddy.models.TruthTable
import kotlin.math.pow

class ConditionChecklistGenerationStrategy : LeafChecklistGeneratorStrategy<PsiExpression> {

    companion object Factory {
        fun create(): ConditionChecklistGenerationStrategy {
            return ConditionChecklistGenerationStrategy()
        }
    }

    override fun generateChecklist(psiElement: PsiExpression): List<TestingChecklistLeafNode> {

        val (simplified, assignments) = PropositionalExpression(psiElement).simplified()

        val testCases = mcdc(assignments.keys.toList(), simplified)

        return testCases.map {

            var description = "Test where in the condition \"${psiElement.text}\", "
            for ((proposition, value) in it.entries) {
                description += "\"${assignments[proposition]}\" is $value, "
            }

            // remove trailing comma and space
            TestingChecklistLeafNode(description.dropLast(2), psiElement)
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
    fun mcdc(variables: List<String>, expressionString: String): Set<Map<String, Boolean>> {

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

        return rows.map { truthTable.assignments(it) }.toSet()
    }
}
