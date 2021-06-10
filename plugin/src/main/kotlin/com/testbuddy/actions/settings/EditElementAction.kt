package com.testbuddy.actions.settings

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.treeStructure.Tree

class EditElementAction : AnAction() {

    public var tree: Tree? = null
    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    override fun actionPerformed(e: AnActionEvent) {
        tree!!.startEditingAtPath(tree!!.selectionPath)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = (tree != null && tree!!.selectionCount > 0)
    }

    fun init(tree: Tree) {
        this.tree = tree
    }
}
