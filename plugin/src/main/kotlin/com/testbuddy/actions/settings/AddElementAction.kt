package com.testbuddy.actions.settings

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.treeStructure.Tree
import com.testbuddy.models.ParameterSuggestionTreeModel

class AddElementAction : AnAction() {
    var tree: Tree? = null
    /**
     * Adds a new case element and starts editing the element node.
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
        (tree!!.model as ParameterSuggestionTreeModel).addPathElement(path)
        tree!!.startEditingAtPath(path)
    }

    /**
     * Updates the state of the action.
     * The action is enabled only if the tree is not null and either class or element nodes were selected.
     *
     * @param e Carries information on the invocation place
     */
    override fun update(e: AnActionEvent) {
        // Path to leaf = [Root, Class, Element] == (Size 3)
        e.presentation.isEnabled = (tree != null && tree!!.selectionCount > 0 && tree!!.selectionPath!!.pathCount >= 2)
    }

    /**
     * Sets the tree attribute which is used for accessing tree functions.
     */
    fun init(tree: Tree) {
        this.tree = tree
    }
}
