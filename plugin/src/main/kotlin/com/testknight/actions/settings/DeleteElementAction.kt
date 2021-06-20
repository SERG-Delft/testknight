package com.testknight.actions.settings

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.treeStructure.Tree
import com.testknight.models.ParameterSuggestionTreeModel

class DeleteElementAction : AnAction() {
    var tree: Tree? = null
    /**
     * Removes the selected node.
     *
     * @param e Carries information on the invocation place
     */
    override fun actionPerformed(e: AnActionEvent) {
        (tree!!.model as ParameterSuggestionTreeModel).removePathElement(tree!!.selectionPath)
    }

    /**
     * Updates the state of the action.
     *The action is enabled only if the tree is not null and atleast one node has been selected.
     *
     * @param e Carries information on the invocation place
     */
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = (tree != null && tree!!.selectionCount > 0)
    }

    /**
     * Sets the tree attribute which is used for accessing tree functions.
     */
    fun init(tree: Tree) {
        this.tree = tree
    }
}
