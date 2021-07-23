package com.testknight.actions.checklist

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.testknight.services.checklist.ChecklistTreeService

class ClearChecklistAction : AnAction() {

    /**
     * Clears the Checklist tree.
     *
     * @param event Event received when the associated menu item is chosen.
     */
    override fun actionPerformed(event: AnActionEvent) {
        actionPerformed(event.project!!)
    }

    /**
     * Clears the Checklist tree.
     *
     * @param project current open project.
     */
    private fun actionPerformed(project: Project) {
        val checklistTreeService = project.service<ChecklistTreeService>()
        checklistTreeService.resetTree()
    }

    /**
     * Determines whether this action button is available for the current context.
     * Requires a project to be open.
     *
     * @param e Event received when the associated group-id menu is chosen.
     */
    override fun update(e: AnActionEvent) {
        // Set the availability if project is open
        e.presentation.isEnabled = e.project != null
    }
}
