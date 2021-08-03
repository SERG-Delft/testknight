package com.testknight.listeners.testlist

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.testknight.actions.testlist.TestListTraceabilityAction
import com.testknight.exceptions.CorruptedTraceFileException
import com.testknight.exceptions.NoTestCoverageDataException
import com.testknight.exceptions.TraceFileNotFoundException
import com.testknight.models.TestMethodUserObject
import com.testknight.services.ExceptionHandlerService
import com.testknight.services.TestTracingService
import javax.swing.event.TreeSelectionEvent
import javax.swing.event.TreeSelectionListener
import javax.swing.tree.DefaultMutableTreeNode

class TestListSelectionListener(
    private val project: Project,
    private val traceabilityButton: TestListTraceabilityAction
) : TreeSelectionListener {

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
                tracingService
                    .highlightTest("${testUserObject.reference.testClassName},${testUserObject.reference.name}")
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
