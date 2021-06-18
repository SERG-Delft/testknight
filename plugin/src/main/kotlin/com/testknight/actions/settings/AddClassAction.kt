package com.testknight.actions.settings

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.treeStructure.Tree
import com.testknight.models.ParameterSuggestionTreeModel
import javax.swing.tree.TreePath

class AddClassAction : AnAction() {
    var tree: Tree? = null

    /**
     * Adds a new class node and starts editing the class node.
     *
     * @param e Carries information on the invocation place
     */
    override fun actionPerformed(e: AnActionEvent) {

        val path = TreePath((tree!!.model as ParameterSuggestionTreeModel).root).pathByAddingChild("NewClass")
        (tree!!.model as ParameterSuggestionTreeModel).addPathElement(path)
        tree!!.startEditingAtPath(path)
    }

    /**
     * Updates the state of the action.
     * The action is enabled only if the tree is not null.
     *
     * @param e Carries information on the invocation place
     */
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = (tree != null)
    }

    /**
     * Sets the tree attribute which is used for accessing tree functions.
     */
    fun init(tree: Tree) {
        this.tree = tree
    }
}
