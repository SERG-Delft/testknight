package com.testknight.models

import com.testknight.extensions.TestKnightTestCase
import junit.framework.TestCase
import org.junit.Test
import javax.swing.tree.TreePath

internal class ParameterSuggestionTreeModelTest: TestKnightTestCase() {

    private val parameterSuggestionMap = mapOf(
        "String" to mutableListOf("\"\"", "null", "a"),
        "int" to mutableListOf("1", "2", "0")
    )

    @Test
    fun testGetChildrenByString() {
        val tree = ParameterSuggestionTreeModel(parameterSuggestionMap.toMutableMap())
        val expected = listOf("\"\"", "null", "a")
        val actual = tree.getChildren("String")
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testGetChildrenByNode() {
        val tree = ParameterSuggestionTreeModel(parameterSuggestionMap.toMutableMap())
        val expected = listOf("String", "int")
        val actual = tree.getChildren(tree.root)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testAddPathElement() {
        val tree = ParameterSuggestionTreeModel(parameterSuggestionMap.toMutableMap())
        val path = TreePath(tree.root).pathByAddingChild("int").pathByAddingChild("3")
        tree.addPathElement(path)
        val expected = listOf("1", "2", "0", "3")
        val actual = tree.getChildren("int")
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testAddPathElementRoot() {
        val tree = ParameterSuggestionTreeModel(parameterSuggestionMap.toMutableMap())
        val path = TreePath(tree.root)
        tree.addPathElement(path)
        val expected = listOf("1", "2", "0", "3")
        val actual = tree.getChildren("int")
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testRemovePathElement() {
        val tree = ParameterSuggestionTreeModel(parameterSuggestionMap.toMutableMap())
        val path = TreePath(tree.root).pathByAddingChild("int").pathByAddingChild("2")
        tree.removePathElement(path)
        val expected = listOf("1", "0")
        val actual = tree.getChildren("int")
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testValueForPathChanged() {
        val tree = ParameterSuggestionTreeModel(parameterSuggestionMap.toMutableMap())
        val path = TreePath(tree.root).pathByAddingChild("int").pathByAddingChild("2")
        tree.valueForPathChanged(path, "3")
        val expected = listOf("1", "3", "0")
        val actual = tree.getChildren("int")
        TestCase.assertEquals(expected, actual)
    }


}
