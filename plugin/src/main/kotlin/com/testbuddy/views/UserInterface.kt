package com.testbuddy.views

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.psi.PsiManager
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.TableSpeedSearch
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.table.JBTable
import com.intellij.ui.treeStructure.Tree
import com.testbuddy.actions.ShowCoverageDiffAction
import com.testbuddy.listeners.CheckListKeyboardListener
import com.testbuddy.listeners.CheckedNodeListener
import com.testbuddy.listeners.ChecklistMouseListener
import com.testbuddy.listeners.ChecklistSelectionListener
import com.testbuddy.listeners.PsiTreeListener
import com.testbuddy.listeners.TestListKeyboardListener
import com.testbuddy.listeners.TestListMouseListener
import com.testbuddy.services.ChecklistTreeService
import com.testbuddy.services.LoadTestsService
import com.testbuddy.utilities.UserInterfaceHelper
import com.testbuddy.views.trees.ChecklistCellEditor
import com.testbuddy.views.trees.TestListCellRenderer
import org.jetbrains.annotations.NotNull
import java.awt.Component
import java.awt.Insets
import java.util.Vector
import javax.swing.table.DefaultTableModel
import javax.swing.tree.DefaultMutableTreeNode

class UserInterface(val project: Project) {

    private var mainUI: JBTabbedPane? = null
    private var testCaseTree: Tree? = null

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
        val toolWindowPanel = SimpleToolWindowPanel(true, true)

        // Setting up the action groups for the toolbar
        val actionManager = ActionManager.getInstance()
        val actionGroup = DefaultActionGroup("ChecklistTabActions", false)
        actionGroup.add(actionManager.getAction("ChecklistAction"))
        actionGroup.add(actionManager.getAction("ClearChecklistAction"))
        actionGroup.addSeparator()
        actionGroup.add(actionManager.getAction("DeleteChecklistAction"))
        actionGroup.add(actionManager.getAction("AddItemChecklistAction"))
        actionGroup.add(actionManager.getAction("EditItemChecklistAction"))
        actionGroup.add(actionManager.getAction("GenerateMethodChecklistAction"))
        val actionToolbar = actionManager.createActionToolbar("ChecklistToolbar", actionGroup, true)
        toolWindowPanel.toolbar = actionToolbar.component

        val service = project.service<ChecklistTreeService>()
        val panel = JBScrollPane()
        // val root = CheckedTreeNode("root")

        service.initUiTree()
        val checkListTree = service.getUiTree()

        // checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        val mouseListener = ChecklistMouseListener(checkListTree, project)
        checkListTree.addMouseListener(mouseListener)

        checkListTree.addCheckboxTreeListener(CheckedNodeListener())
        checkListTree.addKeyListener(CheckListKeyboardListener(checkListTree, project))
        checkListTree.addTreeSelectionListener(ChecklistSelectionListener(project))

        checkListTree.cellEditor = ChecklistCellEditor()
        checkListTree.isEditable = true

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
    private fun getTestListTab(): Component {

        val toolWindowPanel = SimpleToolWindowPanel(true, true)

        // Setting up the action groups for the toolbar
        val actionManager = ActionManager.getInstance()
        val actionGroup = DefaultActionGroup("TestListTabActions", false)
        actionGroup.add(actionManager.getAction("LoadTestAction"))
        actionGroup.add(actionManager.getAction("ClearTestAction"))
        val actionToolbar = actionManager.createActionToolbar("TestListToolbar", actionGroup, true)
        toolWindowPanel.toolbar = actionToolbar.component

        val panel = JBScrollPane()

        val root = DefaultMutableTreeNode("root")

        testCaseTree = Tree(root)

        val cellRenderer = TestListCellRenderer()
        testCaseTree!!.cellRenderer = cellRenderer
        testCaseTree!!.isEditable = false
        testCaseTree!!.isRootVisible = false
        testCaseTree!!.showsRootHandles = true

        val mouseListener = TestListMouseListener(testCaseTree!!, cellRenderer)
        val keyboardListener = TestListKeyboardListener(testCaseTree!!, project)
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

        val toolWindowPanel = SimpleToolWindowPanel(true, true)

        // Setting up the action groups for the toolbar
        val actionManager = ActionManager.getInstance()
        val actionGroup = DefaultActionGroup("CoverageActions", false)
        actionGroup.add(actionManager.getAction("LoadCoverageAction"))
        actionGroup.addSeparator()
        actionGroup.add(actionManager.getAction("ShowIntegratedView"))
        actionGroup.add(actionManager.getAction("HideIntegratedView"))
        val actionToolbar = actionManager.createActionToolbar("CoverageToolbar", actionGroup, true)
        toolWindowPanel.toolbar = actionToolbar.component

        val panel = ScrollPaneFactory.createScrollPane()

        val vec = Vector<String>()
        vec.add("Element")
        vec.add("Line Coverage")
        vec.add("")
        // Create un-editable table, except for buttons.
        val table = JBTable(object : DefaultTableModel(vec, 0) {
            override fun isCellEditable(row: Int, column: Int): Boolean {
                if (column == 2) {
                    return true
                }
                return false
            }
        })

        table.columnModel.getColumn(1).cellRenderer = CoverageStatsCellRenderer()
        ButtonColumn(table, ShowCoverageDiffAction(table, project), 2, project)
        table.tableHeader.reorderingAllowed = false
        val speedSearch = TableSpeedSearch(table)
        speedSearch.setClearSearchOnNavigateNoMatch(true)
        table.autoCreateRowSorter = true

        panel.viewport.view = table
        toolWindowPanel.setContent(panel)

        return toolWindowPanel
    }

    /**
     * Constructor which sets up the MainUI.
     * The MainUI will be a Tabbed pane with a testList and Checklist tab.
     */
    init {
        mainUI = JBTabbedPane(JBTabbedPane.TOP, JBTabbedPane.SCROLL_TAB_LAYOUT)
        mainUI!!.tabComponentInsets = Insets(0, 0, 0, 0)

        // Function call which returns the tab for copy paste
        mainUI!!.addTab("Test List", getTestListTab())
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
