package com.testbuddy.views

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.treeStructure.Tree
import com.testbuddy.services.LoadTestsService
import com.testbuddy.views.actions.LoadTestAction
import com.testbuddy.views.listeners.CopyPasteKeyboardListener
import com.testbuddy.views.listeners.CopyPasteMouseListener
import com.testbuddy.views.trees.ChecklistCellRenderer
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
        actionGroup.add(actionManager.getAction("LoadTestAction"))
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

        val mouseListener = CopyPasteMouseListener(testCaseTree!!, cellRenderer)
        val keyboardListener = CopyPasteKeyboardListener(testCaseTree!!, project)
        mouseListener.installOn(testCaseTree!!)
        testCaseTree!!.addKeyListener(keyboardListener)

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
                override fun childrenChanged(event: PsiTreeChangeEvent) {
                    refreshTestCaseUI(project)
                }
            },
            loadTestsService
        )

        val messageBus = project.messageBus
        messageBus.connect()
            .subscribe(
                FileEditorManagerListener.FILE_EDITOR_MANAGER,
                object : FileEditorManagerListener {
                    override fun fileOpened(@NotNull source: FileEditorManager, @NotNull file: VirtualFile) {
                        refreshTestCaseUI(project)
                    }

                    override fun selectionChanged(@NotNull event: FileEditorManagerEvent) {
                        refreshTestCaseUI(project)
                    }
                }
            )
    }

    companion object {
        /**
         * Updates the CopyPasteTab by calling the LoadTestAction.
         * Uses the project to get editor and psi file information.
         *
         * @param project the current project.
         */
        fun refreshTestCaseUI(project: Project) {
            val editorList = FileEditorManager.getInstance(project).selectedEditors

            if (editorList.isNotEmpty()) {
                val textEditor = editorList[0] as TextEditor

                val psiFile: PsiFile? = PsiManager.getInstance(project).findFile(textEditor.file!!)
                LoadTestAction().actionPerformed(project, psiFile, textEditor.editor)
            }
        }
    }
}
