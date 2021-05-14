package com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiParameter
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class ParameterChecklistGenerationStrategy private constructor(private val typeChecklistCaseMap: Map<String, List<String>>) :
    LeafChecklistGeneratorStrategy<PsiParameter> {

    companion object Factory {
        //we can use this to save some defaults so even if the users removes all the cases
        //they can later restore to the default settings
        //in the future we can possibly extract this to a file where we store all the "defaults"
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
            "byte[]" to listOf("[]"),
            "short[]" to listOf("[]"),
            "int[]" to listOf("null", "[1,2,3,4]", "[4,3,2,1]", "[]"),
            "long[]" to listOf("[]"),
            "float[]" to listOf("[]"),
            "double[]" to listOf("[]"),
            "char[]" to listOf("[]"),
            "boolean[]" to listOf("[]"),
            "String[]" to listOf("[]")
        )

        fun create(): ParameterChecklistGenerationStrategy {
            return ParameterChecklistGenerationStrategy(getTypesCasesMap())
        }

        fun create(typeChecklistCaseMap: Map<String, List<String>>): ParameterChecklistGenerationStrategy {
            return ParameterChecklistGenerationStrategy(typeChecklistCaseMap);
        }

        private fun getTypesCasesMap(): Map<String, List<String>> {
            //the assignment here is a placeholder that we will change
            // once the different cases need to be loaded from a file.
            val map = defaultMap
            return map
        }
    }

    override fun generateChecklist(psiElement: PsiParameter): List<TestingChecklistLeafNode> {
        val typeOfParameter = psiElement.type.canonicalText
        val nameOfParameter = psiElement.name
        val casesForType = this.typeChecklistCaseMap[typeOfParameter] ?: emptyList()
        return casesForType.map { TestingChecklistLeafNode("Test $nameOfParameter for: $it", psiElement) }
    }
}
