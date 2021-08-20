package com.testknight.models

import com.intellij.ui.tree.BaseTreeModel
import com.testknight.exceptions.InvalidTreePathException
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
            checkPath(path, true)
            val className = path.lastPathComponent as String

            // Item already exists, don't have to reset or add it again.
            if (paramSuggestionMap.containsKey(className)) {
                return
            }

            paramSuggestionMap[className] = mutableListOf()

            val index = getIndexOfChild(rootNode, className)
            treeStructureChanged(path.parentPath, intArrayOf(index), arrayOf(className))
            treeNodesInserted(path, intArrayOf(index), arrayOf(className))
        } else {
            checkPath(path, false)
            val className = path.parentPath.lastPathComponent as String
            val typeName = path.lastPathComponent as String

            // Item already exists, don't have to reset or add it again.
            if (paramSuggestionMap.containsKey(className) &&
                paramSuggestionMap[className]!!.contains(typeName)
            ) {
                return
            }

            paramSuggestionMap[className]?.add(typeName)
                ?: throw InvalidTreePathException("Class doesn't exist in Parameter Suggestion")

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
            checkPath(path, true)
            val className = path.lastPathComponent as String

            val index = getIndexOfChild(rootNode, className)

            if (index == -1) throw InvalidTreePathException("Class doesn't exist in Parameter Suggestion")

            paramSuggestionMap.remove(className)
            treeStructureChanged(path.parentPath, intArrayOf(index), arrayOf(className))
            treeNodesRemoved(path, intArrayOf(index), arrayOf(className))
        } else {
            checkPath(path, false)
            val className = path.parentPath.lastPathComponent as String
            val typeName = path.lastPathComponent as String

            val index = getIndexOfChild(className, typeName)

            if (index == -1) throw InvalidTreePathException("Either Class or Value was not found")

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

    /**
     * Checks if the given TreePath is valid.
     * Throws InvalidTreePathException with reason if its not.
     *
     * Suppressing magic number because of path count checks.
     * @param path The tree path to check.
     * @param isClassNode boolean to do check for class node or type node.
     */
    @SuppressWarnings("MagicNumber")
    private fun checkPath(path: TreePath, isClassNode: Boolean) {

        var throwException = false
        var reason = ""
        if (path.pathCount > 3) {
            reason = "Path is bigger than expected."
            throwException = true
        }
        if (isClassNode) {
            if (path.pathCount != 2) {
                reason = "Invalid path size. Should be 2 for Class nodes."
                throwException = true
            }
            if (path.lastPathComponent !is String) {
                reason = "Class node is not a string."
                throwException = true
            }
        } else {
            if (path.pathCount != 3) {
                reason = "Invalid path size. Should be 3 for Value nodes."
                throwException = true
            }
            if (path.parentPath.lastPathComponent !is String) {
                reason = "Class node is not a string."
                throwException = true
            }
            if (path.lastPathComponent !is String) {
                reason = "Value node is not a string."
                throwException = true
            }
        }
        if (throwException) {
            throw InvalidTreePathException(reason)
        }
    }
}
