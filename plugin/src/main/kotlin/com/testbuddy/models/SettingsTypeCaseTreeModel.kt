package com.testbuddy.models

import com.intellij.ui.tree.BaseTreeModel
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath

class SettingsTypeCaseTreeModel(private var typeCaseMap: MutableMap<String, MutableList<String>>) :
    BaseTreeModel<String>() {
    private val rootNode = DefaultMutableTreeNode("root")
    override fun getRoot(): Any {
        return rootNode
    }

    /**
     * @param parent a tree node
     * @return all children of the specified parent node or `null` if they are not ready yet
     */
    override fun getChildren(parent: Any?): MutableList<out String>? {

        if (parent is DefaultMutableTreeNode) {
            if (parent == rootNode) {
                return typeCaseMap.keys.toMutableList()
            }
        }

        if (parent is String) {
            if (typeCaseMap.containsKey(parent)) {
                return typeCaseMap[parent]
            }
        }

        return null
    }

    fun addPathElement(path: TreePath) {
        if (path.parentPath.lastPathComponent == rootNode) {
            val className = path.lastPathComponent as String
            typeCaseMap[className] = mutableListOf()

            val index = getIndexOfChild(rootNode, className)
            treeStructureChanged(path.parentPath, intArrayOf(index), arrayOf(className))
            treeNodesInserted(path, intArrayOf(index), arrayOf(className))
        } else {
            val className = path.parentPath.lastPathComponent as String
            val typeName = path.lastPathComponent as String

            typeCaseMap[className]!!.add(typeName)

            val index = getIndexOfChild(className, typeName)
            treeStructureChanged(path.parentPath, intArrayOf(index), arrayOf(typeName))
            treeNodesInserted(path, intArrayOf(index), arrayOf(className))
        }
    }

    fun removePathElement(path: TreePath) {
        if (path.parentPath.lastPathComponent == rootNode) {
            val className = path.lastPathComponent as String

            val index = getIndexOfChild(rootNode, className)

            typeCaseMap.remove(className)
            treeStructureChanged(path.parentPath, intArrayOf(index), arrayOf(className))
            treeNodesRemoved(path, intArrayOf(index), arrayOf(className))
        } else {
            val className = path.parentPath.lastPathComponent as String
            val typeName = path.lastPathComponent as String

            val index = getIndexOfChild(className, typeName)

            typeCaseMap[className]!!.removeAt(index)

            treeStructureChanged(path.parentPath, intArrayOf(index), arrayOf(typeName))
            treeNodesRemoved(path, intArrayOf(index), arrayOf(className))
        }
    }

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

            val children = typeCaseMap.remove(className)

            typeCaseMap[value.toString()] = children!!

            treeStructureChanged(path.parentPath, intArrayOf(index), arrayOf(className))
        } else {
            val className = path.parentPath.lastPathComponent as String
            val typeName = path.lastPathComponent as String

            if (getIndexOfChild(className, value.toString()) != -1) {
                // Already exists.
                return
            }

            val index = getIndexOfChild(className, typeName)

            typeCaseMap[className]!![index] = value.toString()

            treeStructureChanged(path.parentPath, intArrayOf(index), arrayOf(typeName))
        }
    }

    fun reload() {
        treeStructureChanged(TreePath(rootNode), null, null)
    }
}
