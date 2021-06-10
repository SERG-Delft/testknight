package com.testbuddy.actions.settings

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.treeStructure.Tree
import com.testbuddy.models.SettingsTypeCaseTreeModel
import javax.swing.tree.TreePath

class AddClassAction : AnAction() {
    var tree: Tree? = null
    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    override fun actionPerformed(e: AnActionEvent) {

        val path = TreePath((tree!!.model as SettingsTypeCaseTreeModel).root).pathByAddingChild("NewClass")
        (tree!!.model as SettingsTypeCaseTreeModel).addPathElement(path)
        tree!!.startEditingAtPath(path)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = (tree != null)
    }

    fun init(tree: Tree) {
        this.tree = tree
    }
}
