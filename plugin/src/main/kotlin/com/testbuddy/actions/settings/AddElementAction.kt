package com.testbuddy.actions.settings

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.treeStructure.Tree
import com.testbuddy.models.SettingsTypeCaseTreeModel

class AddElementAction : AnAction() {
    var tree: Tree? = null
    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    override fun actionPerformed(e: AnActionEvent) {
        val classPath = if (tree!!.selectionPath!!.pathCount == 2) {
            tree!!.selectionPath
        } else {
            tree!!.selectionPath.parentPath
        }

        val path = classPath.pathByAddingChild("NewCase")
        (tree!!.model as SettingsTypeCaseTreeModel).addPathElement(path)
        tree!!.startEditingAtPath(path)
    }

    override fun update(e: AnActionEvent) {
        // Path to leaf = [Root, Class, Element] == (Size 3)
        e.presentation.isEnabled = (tree != null && tree!!.selectionCount > 0 && tree!!.selectionPath!!.pathCount >= 2)
    }

    fun init(tree: Tree) {
        this.tree = tree
    }
}
