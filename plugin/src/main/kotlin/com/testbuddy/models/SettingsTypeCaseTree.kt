package com.testbuddy.models

import com.intellij.ui.tree.BaseTreeModel
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath

class SettingsTypeCaseTree(private val typeCaseMap: MutableMap<String, MutableList<String>>) : BaseTreeModel<DefaultMutableTreeNode>() {
    private val rootNode = DefaultMutableTreeNode("root")
    override fun getRoot(): Any {
        return rootNode
    }

    /**
     * @param parent a tree node
     * @return all children of the specified parent node or `null` if they are not ready yet
     */
    override fun getChildren(parent: Any?): MutableList<out DefaultMutableTreeNode>? {


        if(parent is DefaultMutableTreeNode) {
            if(parent == rootNode) {
                return typeCaseMap.keys.map { DefaultMutableTreeNode(it) }.toMutableList()
            }

            if(parent.userObject is String) {
                val parentName = parent.userObject as String
                if (typeCaseMap.containsKey(parentName)) {
                    return typeCaseMap[parentName]?.map { DefaultMutableTreeNode(it) }.toMutableList()
                }
            }
        }



        return null
    }

    override fun valueForPathChanged(path: TreePath?, value: Any?) {
        if(path == null)
            return

        if(path.parentPath == rootNode) {
            //Logic for changing class name
        } else {
            //Logic for changing type cases.
        }

    }
}