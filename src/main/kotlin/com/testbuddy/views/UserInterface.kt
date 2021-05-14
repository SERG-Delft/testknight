package com.testbuddy.views

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.treeStructure.Tree
import com.testbuddy.com.testbuddy.views.trees.ChecklistCellRenderer
import com.testbuddy.com.testbuddy.views.trees.CopyPasteCellRenderer
import com.testbuddy.com.testbuddy.views.trees.CopyPasteListener
import com.testbuddy.services.LoadTestsService
import com.testbuddy.views.actions.LoadTestAction
import java.awt.Component
import javax.swing.tree.DefaultMutableTreeNode

class UserInterface(val project: Project) {

    private var mainUI: JBTabbedPane? = null
    private var testCaseTree: Tree? = null
    private var checkListTree: CheckboxTree? = null

    /**
     * Gets the component to be displayed on the tool window.
     *
     * @return The JBTabbedPane which will be shown on the tool window.
     */
    fun getContent(): JBTabbedPane? {
        return mainUI
    }

    /**
     * Creates the base UI which has scroll panel and a button which adds checklist to the panel.
     *
     * @return The SimpleToolWindowPanel with action toolbar and scroll panel with tree for checklist.
     */
    private fun createCheckList(): Component {
        val toolWindowPanel = SimpleToolWindowPanel(true)

        // Setting up the action group. Currently has default values which needs to be changed later.
        val actionManager = ActionManager.getInstance()
        val actionGroup = DefaultActionGroup("ACTION_GROUP", false)
        actionGroup.add(actionManager.getAction("checklistAction"))
        val actionToolbar: ActionToolbar = actionManager.createActionToolbar("ACTION_TOOLBAR", actionGroup, true)
        toolWindowPanel.toolbar = actionToolbar.component

        val panel = JBScrollPane()
        val root = CheckedTreeNode("root")

        checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        panel.setViewportView(checkListTree)

        toolWindowPanel.setContent(panel)
        return toolWindowPanel
    }

    /**
     * Creates a tool window panel with a action toolbar and a tree.
     * The tree is wrapped in a scroll pane and the tree shows all the test methods and classes in a file.
     * Only the tree with root is returned for now, the actions deal with adding other nodes to the tree.
     *
     * Custom tree renderer is set here and a listener to check for button clicks is also attached.
     *
     * @return The SimpleToolWindowPanel with action toolbar and scroll panel with tree for test cases.
     */
    private fun getCopyPasteTab(): Component {

        val toolWindowPanel = SimpleToolWindowPanel(true)

        // Setting up the action group. Currently has default values which needs to be changed later.
        val actionManager = ActionManager.getInstance()
        val actionGroup = DefaultActionGroup("ACTION_GROUP", false)
        actionGroup.add(actionManager.getAction("deployAction"))
        val actionToolbar: ActionToolbar = actionManager.createActionToolbar("ACTION_TOOLBAR", actionGroup, true)
        toolWindowPanel.toolbar = actionToolbar.component

        val panel = JBScrollPane()

        val root = DefaultMutableTreeNode("root")

        testCaseTree = Tree(root)

        val cellRenderer = CopyPasteCellRenderer()
        testCaseTree!!.cellRenderer = cellRenderer
        testCaseTree!!.isEditable = false
        testCaseTree!!.isRootVisible = false
        testCaseTree!!.showsRootHandles = true

        val listener = CopyPasteListener(testCaseTree!!, cellRenderer)
        listener.installOn(testCaseTree!!)

        panel.setViewportView(testCaseTree)

        toolWindowPanel.setContent(panel)
        return toolWindowPanel
    }

    /**
     * Constructor which sets up the MainUI.
     * The MainUI will be a Tabbed pane with a CopyPaste and Checklist tab.
     */
    init {
        mainUI = JBTabbedPane(JBTabbedPane.TOP, JBTabbedPane.SCROLL_TAB_LAYOUT)

        // Function call which returns the tab for copy paste
        mainUI!!.addTab("CopyPaste", getCopyPasteTab())
        // Function call which returns the tab for checklist
        mainUI!!.addTab("Checklist", createCheckList())

        val loadTestsService = project.service<LoadTestsService>()

        PsiManager.getInstance(project).addPsiTreeChangeListener(
            object : PsiTreeChangeAdapter() {

                /**
                 * Calls the LoadTestAction to refresh the UI whenever the children classes change.
                 *
                 * @param event Event with the Psi tree change information.
                 */
                override fun childrenChanged(event: PsiTreeChangeEvent) {

                    val editorList = FileEditorManager.getInstance(project).selectedEditors

                    var editor: Editor? = null
                    if (editorList.isNotEmpty()) {
                        editor = (editorList[0] as TextEditor).editor
                    }
                    LoadTestAction().actionPerformed(project, event.file, editor)
                }
            },
            loadTestsService
        )
    }
}
