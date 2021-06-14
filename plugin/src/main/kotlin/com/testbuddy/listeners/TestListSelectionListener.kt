package com.testbuddy.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.testbuddy.actions.testcases.TestListTraceabilityAction
import com.testbuddy.exceptions.CorruptedTraceFileException
import com.testbuddy.exceptions.NoTestCoverageDataException
import com.testbuddy.exceptions.TraceFileNotFoundException
import com.testbuddy.models.TestMethodUserObject
import com.testbuddy.services.ExceptionHandlerService
import com.testbuddy.services.TestTracingService
import javax.swing.event.TreeSelectionEvent
import javax.swing.event.TreeSelectionListener
import javax.swing.tree.DefaultMutableTreeNode

class TestListSelectionListener(private val project: Project, private val traceabilityButton: TestListTraceabilityAction) : TreeSelectionListener {

    private val tracingService = project.service<TestTracingService>()

    /**
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change.
     */
    override fun valueChanged(e: TreeSelectionEvent?) {
        tracingService.removeHighlights()
        if (!traceabilityButton.getSelected()) {
            return
        }

        val component = (e?.newLeadSelectionPath ?: return).lastPathComponent ?: return
        if (component is DefaultMutableTreeNode && component.userObject is TestMethodUserObject) {
            val testUserObject = (component.userObject as TestMethodUserObject)
            try {
                tracingService.highlightTest("${testUserObject.reference.testClassName},${testUserObject.reference.name}")
            } catch (ex: TraceFileNotFoundException) {
                project.service<ExceptionHandlerService>().notify(ex)
                return
            } catch (ex: CorruptedTraceFileException) {
                project.service<ExceptionHandlerService>().notify(ex)
                return
            } catch (ex: NoTestCoverageDataException) {
                project.service<ExceptionHandlerService>().notify(ex)
                return
            }
        }
    }
}
