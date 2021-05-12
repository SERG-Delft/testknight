package com.testbuddy.com.testbuddy.models

import com.udojava.evalex.Expression
import kotlin.math.pow

class TruthTable(private val vars: List<String>, expressionString: String) {

    val n = vars.size
    private val table = arrayOfNulls<Int>(2.0.pow(n).toInt())

    init {
        for (i in table.indices) {

            val expr = Expression(expressionString)

            for ((varI, variableStr) in vars.withIndex()) {
                val variableValue = getBit(i, varI)
                expr.with(variableStr, variableValue.toBigDecimal())
            }

            table[i] = expr.eval().toInt()
        }
    }

    /**
     * Gets the ith bit in n.
     *
     * @param n the number
     * @param i the index of bit to be read
     * @return the ith bit of n
     */
    private fun getBit(n: Int, i: Int): Int {
        return (n shr i) and 1
    }

    /**
     * Get the truth table value at a row.
     *
     * @param row the row
     * @return the value at index i
     */
    fun value(row: Int): Int {
        return table[row]!!
    }

    /**
     * Get the variable assignments for row.
     *
     * @param row the row
     * @return a mapping from variable names to truth values
     */
    fun assignments(row: Int): Map<String, Boolean> {
        val res = HashMap<String, Boolean>()
        for ((varI, variable) in vars.withIndex()) {
            res[variable] = getBit(row, varI) == 1
        }
        return res
    }
}
