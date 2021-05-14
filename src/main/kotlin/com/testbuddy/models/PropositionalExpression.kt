package com.testbuddy.com.testbuddy.models

import com.intellij.openapi.project.ProjectManager
import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiParenthesizedExpression
import com.intellij.psi.PsiPolyadicExpression
import com.intellij.psi.util.PsiPrecedenceUtil
import java.util.LinkedList

class PropositionalExpression(private val psiExpression: PsiExpression) {

    private val psiElementFactory = PsiElementFactory.getInstance(ProjectManager.getInstance().defaultProject)
    private val literalTrue = psiElementFactory.createExpressionFromText("1", null)
    private val literalFalse = psiElementFactory.createExpressionFromText("0", null)

    /**
     * Return true if the precedence of the expression is less than that of an and operation
     *
     * @param expression the expression
     * @return true if the precedence of the expression is less than that of an and operation
     */
    private fun isLowPrecedence(expression: PsiExpression): Boolean {
        return PsiPrecedenceUtil.getPrecedence(expression) < PsiPrecedenceUtil.BINARY_AND_PRECEDENCE
    }

    /**
     * Returns true if the expression can be treated as a propositional literal.
     *
     * @param expression the expression
     * @return true if the expression is not made up of other propositions
     */
    private fun isProp(expression: PsiExpression): Boolean {
        return when (expression) {
            is PsiBinaryExpression -> isLowPrecedence(expression)
            is PsiPolyadicExpression -> isLowPrecedence(expression)
            is PsiParenthesizedExpression -> false
            else -> true
        }
    }

    /**
     * Performs a preorder traversal of the expression and replaces all propositions with an identifier
     *
     * @return the expression with all propositions simplified, and a map containing the variable assignments
     */
    @Suppress("ReturnCount") // could be refactored but the code woudl be much more complex
    fun simplified(): Pair<String, Map<String, String>> {

        // a copy of the expression we can use for
        val simplifiedExpr = psiExpression.copy() as PsiExpression

        // this map will keep the relationships between variables in the end result and their text form in the code
        val assignments = HashMap<String, String>()
        var id = 0

        // special case for the root since .replace() does not work for root elements
        when {
            simplifiedExpr is PsiLiteralExpression -> when (simplifiedExpr.text) {
                "true" -> return Pair("1", assignments)
                "false" -> return Pair("0", assignments)
            }
            isProp(simplifiedExpr) -> {
                assignments["PROP0"] = simplifiedExpr.text
                return Pair("PROP0", assignments)
            }
        }

        // stack for iterative preorder traversal
        val stack = LinkedList<PsiExpression>(); stack.push(simplifiedExpr)

        while (stack.isNotEmpty()) {

            val node = stack.pop()

            when {

                // if the expression is a literal replace it with 1/0 instead of true/false
                node is PsiLiteralExpression -> when (node.text) {
                    "true" -> node.replace(literalTrue)
                    "false" -> node.replace(literalFalse)
                }

                // if the expression is a proposition replace it with an identifier
                isProp(node) -> {
                    val propId = "PROP$id"; id++
                    assignments[propId] = node.text

                    node.replace(psiElementFactory.createIdentifier(propId))
                }

                // for parenthesized expressions, push the inner expression
                node is PsiParenthesizedExpression -> stack.push(node.expression)

                // for binary/polyadic expression push each operand
                node is PsiBinaryExpression || node is PsiPolyadicExpression -> {
                    (node as PsiPolyadicExpression).operands.forEach { stack.push(it) }
                }
            }
        }

        // return the expression text as well as the variable assignments
        return Pair(simplifiedExpr.text, assignments)
    }
}
