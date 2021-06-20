package com.testknight.actions.testcases

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.components.service
import com.intellij.ui.treeStructure.Tree
import com.testknight.exceptions.CorruptedTraceFileException
import com.testknight.exceptions.NoTestCoverageDataException
import com.testknight.exceptions.TraceFileNotFoundException
import com.testknight.models.TestMethodUserObject
import com.testknight.services.ExceptionHandlerService
import com.testknight.services.TestTracingService
import javax.swing.tree.DefaultMutableTreeNode

class TestListTraceabilityAction : ToggleAction() {

    private var selected = false
    private lateinit var tree: Tree

    /**
     * Returns the state value (true/false)
     *
     * @param e the AnActionEvent which triggers the action.
     * @return true if the action is selected, false otherwise
     */
    override fun isSelected(e: AnActionEvent): Boolean {
        return selected
    }

    /**
     * Sets the selected variable to the value of the state.
     *
     * @param e the AnActionEvent which changes the selected variable.
     * @param state the new selected state of the selected variable.
     */
    override fun setSelected(e: AnActionEvent, state: Boolean) {
        selected = state

        if (!state) {
            e.presentation.text = "Show Lines Covered"
            e.project?.service<TestTracingService>()?.removeHighlights() ?: return
        } else {
            e.presentation.text = "Hide Lines Covered"
            val component = (tree.lastSelectedPathComponent ?: return)
            if (component is DefaultMutableTreeNode && component.userObject is TestMethodUserObject) {
                val testUserObject = (component.userObject as TestMethodUserObject)
                try {
                    e.project?.service<TestTracingService>()
                        ?.highlightTest("${testUserObject.reference.testClassName},${testUserObject.reference.name}")
                } catch (ex: TraceFileNotFoundException) {
                    service<ExceptionHandlerService>().notify(ex)
                    return
                } catch (ex: CorruptedTraceFileException) {
                    service<ExceptionHandlerService>().notify(ex)
                    return
                } catch (ex: NoTestCoverageDataException) {
                    service<ExceptionHandlerService>().notify(ex)
                    return
                }
            }
        }
    }

    /**
     * Getter for selected private attribute.
     *
     * @return true if the traceability is selected, false otherwise
     */
    fun getSelected(): Boolean {
        return selected
    }

    /**
     * Setter for the tree.
     *
     * @param tree the new value for the tree.
     */
    fun setTree(tree: Tree) {
        this.tree = tree
    }
}
