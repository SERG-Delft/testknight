package com.testbuddy.models

import com.intellij.ui.tree.BaseTreeModel
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath

class ParameterSuggestionTreeModel(private var paramSuggestionMap: MutableMap<String, MutableList<String>>) :
    BaseTreeModel<String>() {
    private val rootNode = DefaultMutableTreeNode("root")

    /**
     * Returns the root node
     */
    override fun getRoot(): Any {
        return rootNode
    }

    /**
     * gets the children of a given parent node.
     *
     * @param parent a tree node
     * @return all children of the specified parent node or `null` if they are not ready yet
     */
    override fun getChildren(parent: Any?): MutableList<out String>? {

        if (parent is DefaultMutableTreeNode) {
            if (parent == rootNode) {
                return paramSuggestionMap.keys.toMutableList()
            }
        }

        if (parent is String) {
            if (paramSuggestionMap.containsKey(parent)) {
                return paramSuggestionMap[parent]
            }
        }

        return null
    }

    /**
     * Adds the node pointed by the TreePath.
     *
     * @param path The path of the node to be added.
     */
    fun addPathElement(path: TreePath) {
        if (path.parentPath.lastPathComponent == rootNode) {
            val className = path.lastPathComponent as String
            paramSuggestionMap[className] = mutableListOf()

            val index = getIndexOfChild(rootNode, className)
            treeStructureChanged(path.parentPath, intArrayOf(index), arrayOf(className))
            treeNodesInserted(path, intArrayOf(index), arrayOf(className))
        } else {
            val className = path.parentPath.lastPathComponent as String
            val typeName = path.lastPathComponent as String

            paramSuggestionMap[className]!!.add(typeName)

            val index = getIndexOfChild(className, typeName)
            treeStructureChanged(path.parentPath, intArrayOf(index), arrayOf(typeName))
            treeNodesInserted(path, intArrayOf(index), arrayOf(className))
        }
    }

    /**
     * Removes the node pointed by the TreePath.
     *
     * @param path The path of the node to be removed.
     */
    fun removePathElement(path: TreePath) {
        if (path.parentPath.lastPathComponent == rootNode) {
            val className = path.lastPathComponent as String

            val index = getIndexOfChild(rootNode, className)

            paramSuggestionMap.remove(className)
            treeStructureChanged(path.parentPath, intArrayOf(index), arrayOf(className))
            treeNodesRemoved(path, intArrayOf(index), arrayOf(className))
        } else {
            val className = path.parentPath.lastPathComponent as String
            val typeName = path.lastPathComponent as String

            val index = getIndexOfChild(className, typeName)

            paramSuggestionMap[className]!!.removeAt(index)

            treeStructureChanged(path.parentPath, intArrayOf(index), arrayOf(typeName))
            treeNodesRemoved(path, intArrayOf(index), arrayOf(className))
        }
    }

    /**
     * Receives information about edited nodes and updates it according to the data model we have.
     *
     * @param path The path which got modified
     * @param value the new modified value
     */
    override fun valueForPathChanged(path: TreePath?, value: Any?) {

        if (path == null || value == null || value.toString() == "") {
            return
        }

        if (path.parentPath.lastPathComponent == rootNode) {
            val className = path.lastPathComponent as String

            if (getIndexOfChild(rootNode, value.toString()) != -1) {
                return
            }

            val index = getIndexOfChild(rootNode, className)

            val children = paramSuggestionMap.remove(className)

            paramSuggestionMap[value.toString()] = children!!

            treeStructureChanged(path.parentPath, intArrayOf(index), arrayOf(className))
        } else {
            val className = path.parentPath.lastPathComponent as String
            val typeName = path.lastPathComponent as String

            if (getIndexOfChild(className, value.toString()) != -1) {
                // Already exists.
                return
            }

            val index = getIndexOfChild(className, typeName)

            paramSuggestionMap[className]!![index] = value.toString()

            treeStructureChanged(path.parentPath, intArrayOf(index), arrayOf(typeName))
        }
    }

    /**
     * Used to refresh the Tree shown in the user interface.
     *
     * Passes a TreeStructureChanged event, with the rootnode being the parent node.
     * Causes the whole tree to get updated (UI Wise).
     */
    fun reload() {
        // It is important to have children null (at least)
        // so that the listener doesn't think that that the tree got updated.
        treeStructureChanged(TreePath(rootNode), null, null)
    }
}
