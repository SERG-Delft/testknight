package com.testbuddy.com.testbuddy.models

import java.util.Locale
import javax.script.ScriptEngineManager
import javax.script.SimpleBindings
import kotlin.math.pow

class TruthTable(private val vars: List<String>, expressionString: String) {

    private val n = vars.size
    private val table = arrayOfNulls<Boolean>(2.0.pow(n).toInt())
    private val engine = ScriptEngineManager().getEngineByName("groovy")

    init {
        for (i in table.indices) {

            val bindings = SimpleBindings()

            for ((varI, varStr) in vars.withIndex()) {
                bindings[varStr] = getBit(i, varI)
            }

            table[i] = engine.eval(expressionString, bindings) as Boolean?
        }
    }

    /**
     * Gets the ith bit in n.
     *
     * @param num the number
     * @param i the index of bit to be read
     * @return the ith bit of n
     */
    private fun getBit(num: Int, i: Int): Boolean {
        val binStr = String.format(
            Locale.getDefault(),
            "%${n}s",
            Integer.toBinaryString(num)
        ).replace(' ', '0')
        return binStr[i] == '1'
    }

    /**
     * Get the truth table value at a row.
     *
     * @param row the row
     * @return the value at index i
     */
    fun value(row: Int): Boolean {
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
            res[variable] = getBit(row, varI)
        }
        return res
    }
}
