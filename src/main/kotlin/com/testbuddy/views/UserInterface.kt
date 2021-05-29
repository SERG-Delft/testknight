package com.testbuddy.views

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.psi.PsiManager
import com.intellij.ui.CheckboxTree
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.treeStructure.Tree
import com.testbuddy.services.ChecklistTreeService
import com.testbuddy.services.LoadTestsService
import com.testbuddy.utilities.UserInterfaceHelper
import com.testbuddy.views.listeners.CheckListKeyboardListener
import com.testbuddy.views.listeners.CheckedNodeListener
import com.testbuddy.views.listeners.ChecklistMouseListener
import com.testbuddy.views.listeners.ChecklistSelectionListener
import com.testbuddy.views.listeners.CopyPasteKeyboardListener
import com.testbuddy.views.listeners.CopyPasteMouseListener
import com.testbuddy.views.listeners.PsiTreeListener
import com.testbuddy.views.trees.CopyPasteCellRenderer
import org.jetbrains.annotations.NotNull
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

        // Setting up the action groups for the toolbar
        val actionManager = ActionManager.getInstance()
        val actionGroup = DefaultActionGroup("ChecklistTabActions", false)
        actionGroup.add(actionManager.getAction("ChecklistAction"))
        actionGroup.add(actionManager.getAction("ClearChecklistAction"))
        val actionToolbar = actionManager.createActionToolbar("ChecklistToolbar", actionGroup, true)
        toolWindowPanel.toolbar = actionToolbar.component

        val service = project.service<ChecklistTreeService>()
        val panel = JBScrollPane()
        // val root = CheckedTreeNode("root")

        service.initUiTree()
        val checkListTree = service.getUiTree()

        // checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        val mouseListener = ChecklistMouseListener(checkListTree!!, project)
        checkListTree!!.addMouseListener(mouseListener)

        checkListTree!!.addCheckboxTreeListener(CheckedNodeListener())
        checkListTree!!.addKeyListener(CheckListKeyboardListener(checkListTree!!))
        checkListTree!!.addTreeSelectionListener(ChecklistSelectionListener(project))

        // service.initTrees(checkListTree!!)
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

        // Setting up the action groups for the toolbar
        val actionManager = ActionManager.getInstance()
        val actionGroup = DefaultActionGroup("CopyPasteTabActions", false)
        actionGroup.add(actionManager.getAction("LoadTestAction"))
        actionGroup.add(actionManager.getAction("ClearTestAction"))
        val actionToolbar = actionManager.createActionToolbar("CopyPasteToolbar", actionGroup, true)
        toolWindowPanel.toolbar = actionToolbar.component

        val panel = JBScrollPane()

        val root = DefaultMutableTreeNode("root")

        testCaseTree = Tree(root)

        val cellRenderer = CopyPasteCellRenderer()
        testCaseTree!!.cellRenderer = cellRenderer
        testCaseTree!!.isEditable = false
        testCaseTree!!.isRootVisible = false
        testCaseTree!!.showsRootHandles = true

        val mouseListener = CopyPasteMouseListener(testCaseTree!!, cellRenderer)
        val keyboardListener = CopyPasteKeyboardListener(testCaseTree!!, project)
        mouseListener.installOn(testCaseTree!!)
        testCaseTree!!.addKeyListener(keyboardListener)

        panel.setViewportView(testCaseTree)

        toolWindowPanel.setContent(panel)

        return toolWindowPanel
    }

    /**
     * Creates a tool window panel with a action toolbar for Coverage tab.
     *
     * @return The SimpleToolWindowPanel with action toolbar and
     * scroll panel where the coverage statistics will be shown.
     */
    private fun createCoverage(): Component {

        val toolWindowPanel = SimpleToolWindowPanel(true)

        // Setting up the action groups for the toolbar
        val actionManager = ActionManager.getInstance()
        val actionGroup = DefaultActionGroup("CoverageActions", false)
        actionGroup.add(actionManager.getAction("LoadCoverageAction"))
        actionGroup.add(actionManager.getAction("ShowCoverageDiffAction"))
        actionGroup.addSeparator()
        actionGroup.add(actionManager.getAction("ShowIntegratedView"))
        actionGroup.add(actionManager.getAction("HideIntegratedView"))
        val actionToolbar = actionManager.createActionToolbar("CoverageToolbar", actionGroup, true)
        toolWindowPanel.toolbar = actionToolbar.component

        val panel = JBScrollPane()
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
        // Function call which returns the tab for coverage
        mainUI!!.addTab("Coverage", createCoverage())

        val loadTestsService = project.service<LoadTestsService>()

        PsiManager.getInstance(project).addPsiTreeChangeListener(PsiTreeListener(project), loadTestsService)

        val messageBus = project.messageBus
        messageBus.connect()
            .subscribe(
                FileEditorManagerListener.FILE_EDITOR_MANAGER,
                object : FileEditorManagerListener {
                    override fun selectionChanged(@NotNull event: FileEditorManagerEvent) {
                        UserInterfaceHelper.refreshTestCaseUI(project)
                    }
                }
            )
    }
}
