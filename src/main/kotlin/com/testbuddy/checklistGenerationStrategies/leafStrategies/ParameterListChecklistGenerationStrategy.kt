package com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiParameterList
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class ParameterListChecklistGenerationStrategy private constructor(
    private val typeChecklistCaseMap: Map<String, List<String>>
) :
    LeafChecklistGeneratorStrategy<PsiParameterList> {

    companion object Factory {
        // we can use this to save some defaults so even if the users removes all the cases
        // they can later restore to the default settings
        // in the future we can possibly extract this to a file where we store all the "defaults"
        private val defaultMap = mapOf<String, List<String>>(
            "byte" to listOf("Byte.MAX_VALUE", "Byte.MIN_VALUE"),
            "short" to listOf("Short.MAX_VALUE", "Short.MIN_VALUE"),
            "int" to listOf("1", "0", "Integer.MAX_VALUE", "Integer.MIN_VALUE", "-42"),
            "long" to listOf("0"),
            "float" to listOf("3.14", "0.0", "-3.14", "1.0", "0.00000042", "Float.MAX_VALUE", "Float.MIN_VALUE"),
            "double" to listOf("Double.MAX_VALUE", "Double.MIN_VALUE"),
            "char" to listOf("a", "α", "∅"),
            "boolean" to listOf("true", "false"),
            "String" to listOf("\"\"", "\"a\"", "\"hello world\"", "\"καλήν εσπέραν άρχοντες\"", "\"€\"", "\"₹\""),
            "byte[]" to listOf("[]", "null"),
            "short[]" to listOf("[]", "null"),
            "int[]" to listOf("null", "[1,2,3,4]", "[4,3,2,1]", "[]"),
            "long[]" to listOf("[]", "null"),
            "float[]" to listOf("[]", "null"),
            "double[]" to listOf("[]", "null"),
            "char[]" to listOf("[]", "null"),
            "boolean[]" to listOf("[]", "null"),
            "String[]" to listOf("[]", "null")
        )

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
         * Returns the default mapping between types and test cases.
         * Now it acts as a placeholder but in the future we can
         * change it to read from the file.
         *
         * @return a map associating the types with their test cases.
         */
        private fun getTypesCasesMap(): Map<String, List<String>> {
            // the assignment here is a placeholder that we will change
            // once the different cases need to be loaded from a file.
            val map = defaultMap
            return map
        }
    }

    /**
     * Generates the checklist items for a parameter list.
     *
     * @param psiElement the parameter list
     * @return a list of TestingCheckListLeafNode objects corresponding to the checklist items.
     */
    override fun generateChecklist(psiElement: PsiParameterList): List<TestingChecklistLeafNode> {
        val result = mutableListOf<TestingChecklistLeafNode>()
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
    fun generateChecklistForParameter(psiElement: PsiParameter): List<TestingChecklistLeafNode> {
        val typeOfParameter = psiElement.type.canonicalText
        val nameOfParameter = psiElement.name
        val casesForType = this.typeChecklistCaseMap[typeOfParameter] ?: emptyList()
        return casesForType.map {
            TestingChecklistLeafNode("Test method parameter \"$nameOfParameter\" equal to: $it", psiElement)
        }
    }
}
