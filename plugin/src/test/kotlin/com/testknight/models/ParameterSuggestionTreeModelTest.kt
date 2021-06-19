package com.testknight.models

import com.testknight.exceptions.InvalidTreePathException
import com.testknight.extensions.TestKnightTestCase
import junit.framework.TestCase
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import javax.swing.tree.TreePath

internal class ParameterSuggestionTreeModelTest : TestKnightTestCase() {

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
    fun testAddPathElementClass() {
        val tree = ParameterSuggestionTreeModel(parameterSuggestionMap.toMutableMap())
        val path = TreePath(tree.root).pathByAddingChild("byte")
        tree.addPathElement(path)
        val expected = listOf("String", "int", "byte")
        val actual = tree.getChildren(tree.root)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testAddPathElementValue() {
        val tree = ParameterSuggestionTreeModel(parameterSuggestionMap.toMutableMap())
        val path = TreePath(tree.root).pathByAddingChild("int").pathByAddingChild("3")
        tree.addPathElement(path)
        val expected = listOf("1", "2", "0", "3")
        val actual = tree.getChildren("int")
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testAddPathElementValueBadClass() {
        val tree = ParameterSuggestionTreeModel(parameterSuggestionMap.toMutableMap())
        val path = TreePath(tree.root).pathByAddingChild("byte").pathByAddingChild("3")

        assertThrows<InvalidTreePathException> {
            tree.addPathElement(path)
        }
    }

    @Test
    fun testRemovePathElementClass() {
        val tree = ParameterSuggestionTreeModel(parameterSuggestionMap.toMutableMap())
        val path = TreePath(tree.root).pathByAddingChild("int")
        tree.removePathElement(path)
        val expected = listOf("String")
        val actual = tree.getChildren(tree.root)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testRemovePathElementBadClass() {
        val tree = ParameterSuggestionTreeModel(parameterSuggestionMap.toMutableMap())
        val path = TreePath(tree.root).pathByAddingChild("byte")

        assertThrows<InvalidTreePathException> {
            tree.removePathElement(path)
        }
    }

    @Test
    fun testRemovePathElementValue() {
        val tree = ParameterSuggestionTreeModel(parameterSuggestionMap.toMutableMap())
        val path = TreePath(tree.root).pathByAddingChild("int").pathByAddingChild("2")
        tree.removePathElement(path)
        val expected = listOf("1", "0")
        val actual = tree.getChildren("int")
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testRemovePathElementValueBadClass() {
        val tree = ParameterSuggestionTreeModel(parameterSuggestionMap.toMutableMap())
        val path = TreePath(tree.root).pathByAddingChild("byte").pathByAddingChild("2")

        assertThrows<InvalidTreePathException> {
            tree.removePathElement(path)
        }
    }

    @Test
    fun testValueForPathChangedValue() {
        val tree = ParameterSuggestionTreeModel(parameterSuggestionMap.toMutableMap())
        val path = TreePath(tree.root).pathByAddingChild("int").pathByAddingChild("2")
        tree.valueForPathChanged(path, "3")
        val expected = listOf("1", "3", "0")
        val actual = tree.getChildren("int")
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testValueForPathChangedClass() {
        val tree = ParameterSuggestionTreeModel(parameterSuggestionMap.toMutableMap())
        val path = TreePath(tree.root).pathByAddingChild("int")
        tree.valueForPathChanged(path, "byte")
        val expected = listOf("1", "2", "0")
        val actual = tree.getChildren("byte")
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testCheckPathClassLong() {
        val tree = ParameterSuggestionTreeModel(parameterSuggestionMap.toMutableMap())
        val path = TreePath(tree.root)
            .pathByAddingChild("int")
            .pathByAddingChild("aaa")
            .pathByAddingChild("aaa")
            .pathByAddingChild("daa")

        assertThrows<InvalidTreePathException> {
            tree.valueForPathChanged(path, "byte")
        }
    }
}
