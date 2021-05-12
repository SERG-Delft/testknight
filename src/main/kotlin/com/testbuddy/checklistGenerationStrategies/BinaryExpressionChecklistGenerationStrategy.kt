package com.testbuddy.com.testbuddy.checklistGenerationStrategies

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiParenthesizedExpression
import com.intellij.psi.PsiPolyadicExpression
import com.testbuddy.com.testbuddy.models.TestingChecklistItem
import com.testbuddy.com.testbuddy.models.TruthTable
import java.util.LinkedList
import kotlin.collections.HashMap
import kotlin.math.pow

class BinaryExpressionChecklistGenerationStrategy(val project: Project) : ChecklistGenerator<PsiPolyadicExpression> {

    // Set of operators that evaluate to a boolean
    private val evalToBoolOperators = setOf("GT", "LT", "GE", "LE", "NE", "EQ")

    private val psiElementFactory = PsiElementFactory.getInstance(project)

    companion object Factory {
        fun create(): BinaryExpressionChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiPolyadicExpression): List<TestingChecklistItem> {
        return listOf()
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

    /**
     * Performs a preorder traversal of the to get the list of variables.
     *
     * @param expr a boolean PsiPolyadicExpression
     * @return the expression with all propositions simplified, and a map containing the variable assignments
     */
    @Suppress("NestedBlockDepth")
    fun simplifyExpression(expr: PsiPolyadicExpression): Pair<String, Map<String, String>> {

        // a copy of the expression we can use for
        val simplified = expr.copy() as PsiPolyadicExpression

        // this map will keep the relationships between variables in the end result and their text form in the code
        val assignments = HashMap<String, String>()
        var id = 0

        /**
         * Replaces a proposition in a node with a simple identifier. This makes evaluation much simpler
         *
         * @param node node to be replaced
         */
        fun replace(node: PsiExpression) {
            val propId = "PROP$id"; id++
            assignments[propId] = node.text
            node.replace(psiElementFactory.createIdentifier(propId))
        }

        // stack for iterative preorder traversal
        val stack = LinkedList<PsiPolyadicExpression>(); stack.push(simplified)

        while (stack.isNotEmpty()) {

            val node = stack.pop()

            // replace the node for expressions that evaluate to boolean, such as (a > b)
            if (evalToBoolOperators.contains(node.operationTokenType.toString())) {
                replace(node)
            } else {

                for (operand in node.operands) {

                    when (operand) {
                        // recurse on expressions
                        is PsiPolyadicExpression -> stack.push(operand)
                        is PsiBinaryExpression -> stack.push(operand as PsiPolyadicExpression)
                        is PsiParenthesizedExpression -> stack.push(operand.expression as PsiPolyadicExpression)
                        // replace the node for all other expressions such as method applications
                        else -> replace(operand)
                    }
                }
            }
        }

        // return the expression text as well as the variable assignments
        return Pair(simplified.text, assignments)
    }
}
