package com.testbuddy.settings

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.ColorPanel
import com.intellij.ui.TreeSpeedSearch
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import com.intellij.util.ui.tree.TreeUtil
import com.testbuddy.actions.settings.ResetTreeAction
import com.testbuddy.models.SettingsTypeCaseTreeModel
import javax.swing.JPanel
import javax.swing.event.TreeModelEvent
import javax.swing.event.TreeModelListener
import javax.swing.tree.TreeSelectionModel

class SettingsComponent {

    private var myPanel: DialogPanel
    private val state = SettingsService.instance.state
    var isTypeCaseTreeModified = false
    lateinit var typeCaseTreeInfo: MutableMap<String, MutableList<String>>
    lateinit var typeCaseTreeModel: SettingsTypeCaseTreeModel

    lateinit var addedColor: ColorPanel
    lateinit var deletedColor: ColorPanel
    lateinit var tracedColor: ColorPanel

    // KMutableProperty0 has been changed to Setters and Getters
    // For some reason, the following error was thrown when using KMutableProperty0
    // java.lang.NoSuchMethodError:
    // 'void kotlin.jvm.internal.MutablePropertyReference0Impl.<init>
    // (java.lang.Object, java.lang.Class, java.lang.String, java.lang.String, int)'
    // We can change back to KMutableProperty0 once this error has been fixed.
    init {
        myPanel = panel {
            titledRow("Telemetry") {
                row {
                    checkBox(
                        "Allow data collection",
                        // state.telemetrySettings::isEnabled
                        { state.telemetrySettings.isEnabled },
                        { newVal -> state.telemetrySettings.isEnabled = newVal }
                    )
                }
                row {
                    label("By selecting this, you agree to our", UIUtil.ComponentStyle.SMALL)
                    link("terms and conditions.", UIUtil.ComponentStyle.SMALL) {
                        BrowserUtil.browse("https://youtu.be/dQw4w9WgXcQ")
                    }
                }
            }

            titledRow("Test Duplication") {
                val testListSettings = state.testListSettings

                for (i in testListSettings.highlightStrategies) {
                    row {
                        checkBox(i.key, { i.value }, { newVal -> i.setValue(newVal) })
                    }
                }
            }

            titledRow("Checklist") {
                val checklistSettings = state.checklistSettings
                val coverageComboBox = listOf("MC/DC", "BRANCH")
                val savedSelection = checklistSettings.coverageCriteria
                val comboBoxModel = CollectionComboBoxModel(coverageComboBox, savedSelection)
                row {
                    label("Coverage Criteria")
                    comboBox(
                        comboBoxModel,
                        // checklistSettings::coverageCriteria
                        { checklistSettings.coverageCriteria },
                        { newVal ->
                            if (newVal != null) {
                                checklistSettings.coverageCriteria = newVal
                            }
                        }
                    )
                }

                row {
                    checkBox(
                        "Show gutter icons",
                        // checklistSettings::showGutterIcons
                        { checklistSettings.showGutterIcons },
                        { newVal -> checklistSettings.showGutterIcons = newVal }
                    )
                }
                row {
                    checkBox(
                        "Goto selected checklist item's source",
                        // checklistSettings::gotoChecklistItem
                        { checklistSettings.gotoChecklistItem },
                        { newVal -> checklistSettings.gotoChecklistItem = newVal }
                    )
                }
                row {
                    checkBox(
                        "Highlight selected checklist item's source",
                        // checklistSettings::highlightChecklistItem
                        { checklistSettings.highlightChecklistItem },
                        { newVal -> checklistSettings.highlightChecklistItem = newVal }
                    )
                }

                row("Checklist strategies") {
                    for (i in checklistSettings.checklistStrategies) {
                        row {
                            checkBox(i.key, { i.value }, { newVal -> i.setValue(newVal) })
                        }
                    }
                }

                row("Type Cases") {

                    row {
                        val scrollPanel = JBScrollPane()
                        typeCaseTreeInfo = SettingsService.createTreeDeepCopy(checklistSettings.typeCaseMap)
                        typeCaseTreeModel = SettingsTypeCaseTreeModel(typeCaseTreeInfo)
                        typeCaseTreeModel.addTreeModelListener(object : TreeModelListener {
                            override fun treeNodesChanged(p0: TreeModelEvent?) {
                                if (p0?.children != null) {
                                    isTypeCaseTreeModified = true
                                }
                            }

                            override fun treeNodesInserted(p0: TreeModelEvent?) {
                                if (p0?.children != null) {
                                    isTypeCaseTreeModified = true
                                }
                            }

                            override fun treeNodesRemoved(p0: TreeModelEvent?) {
                                if (p0?.children != null) {
                                    isTypeCaseTreeModified = true
                                }
                            }

                            override fun treeStructureChanged(p0: TreeModelEvent?) {
                                if (p0?.children != null) {
                                    isTypeCaseTreeModified = true
                                }
                            }
                        })
                        val tree = Tree(typeCaseTreeModel)

                        tree.isRootVisible = false
                        tree.showsRootHandles = true
                        tree.isEditable = true
                        tree.selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION

                        TreeUtil.expand(tree, 1)
                        scrollPanel.setViewportView(tree)
                        TreeSpeedSearch(tree)

                        (tree.model as SettingsTypeCaseTreeModel)

                        val actionManager = ActionManager.getInstance()
                        val actionGroup = DefaultActionGroup("TestListTabActions", false)
                        actionGroup.add(actionManager.getAction("AddSettingsItem"))
                        actionGroup.add(actionManager.getAction("DeleteSettingsItem"))
                        actionGroup.add(actionManager.getAction("EditSettingsItem"))
                        val resetAction = actionManager.getAction("ResetSettingsTree")
                        (resetAction as ResetTreeAction).init(this@SettingsComponent)
                        actionGroup.add(resetAction)
                        val actionToolbar =
                            actionManager.createActionToolbar("SettingsTreeToolbar", actionGroup, false)

                        val treePanel: JPanel = JBUI.Panels.simplePanel()
                            .addToCenter(scrollPanel)
                            .addToLeft(actionToolbar.component)

                        treePanel().constraints(CCFlags.growX)
                    }
                }
            }

            titledRow("Coverage") {
                val coverageSettings = state.coverageSettings
                row {
                    checkBox(
                        "Show newly (un)covered lines in gutter",
                        // coverageSettings::showIntegratedView
                        { coverageSettings.showIntegratedView },
                        { newVal -> coverageSettings.showIntegratedView = newVal }
                    )
                }
                row {
                    addedColor = ColorPanel()
                    addedColor.selectedColor = SettingsService.toColor(coverageSettings.addedColor)

                    label("Newly covered lines color: ")
                    addedColor()
                }
                row {
                    deletedColor = ColorPanel()
                    deletedColor.selectedColor = SettingsService.toColor(coverageSettings.deletedColor)

                    label("Newly uncovered lines color: ")
                    deletedColor()
                }
                row {
                    tracedColor = ColorPanel()
                    tracedColor.selectedColor = SettingsService.toColor(coverageSettings.tracedColor)

                    label("Traced tests lines color: ")
                    tracedColor()
                }
            }
        }
    }

    /**
     * Checks if the settings panel has different colors than the settings state.
     * Returns false is any of the newly selected color is null (as we don't want to save null colors)
     *
     * @param coverageColors Settings state's coverage colors
     */
    fun isColorModified(coverageColors: CoverageSettings): Boolean {

        if (this.addedColor.selectedColor == null ||
            this.deletedColor.selectedColor == null ||
            this.tracedColor.selectedColor == null
        ) {
            return false
        }

        val covAddedColor = SettingsService.toColor(coverageColors.addedColor)
        val covDeletedColor = SettingsService.toColor(coverageColors.deletedColor)
        val covTracedColor = SettingsService.toColor(coverageColors.tracedColor)

        return (
            covAddedColor != this.addedColor.selectedColor!! ||
                covDeletedColor != this.deletedColor.selectedColor!! ||
                covTracedColor != this.tracedColor.selectedColor!!
            )
    }

    /**
     * Gets called when the uses press reset in settings.
     * This reverts back to the old color choices in the panel by loading it from the settings state.
     *
     * @param coverageColors Settings state's coverage colors
     */
    fun resetCoverageColors(coverageColors: CoverageSettings) {
        val covAddedColor = SettingsService.toColor(coverageColors.addedColor)
        val covDeletedColor = SettingsService.toColor(coverageColors.deletedColor)
        val covTracedColor = SettingsService.toColor(coverageColors.tracedColor)

        this.addedColor.selectedColor = covAddedColor
        this.deletedColor.selectedColor = covDeletedColor
        this.tracedColor.selectedColor = covTracedColor
    }

    /**
     * Gets called when the uses press apply in settings.
     * This saves the newly selected coverage colors to the settings state.
     *
     * @param coverageColors Settings state's coverage colors
     */
    fun applyCoverageColors(coverageColors: CoverageSettings) {
        coverageColors.addedColor = this.addedColor.selectedColor?.let { SettingsService.toColorHex(it) }
            ?: coverageColors.addedColor
        coverageColors.deletedColor = this.deletedColor.selectedColor?.let { SettingsService.toColorHex(it) }
            ?: coverageColors.deletedColor
        coverageColors.tracedColor = this.tracedColor.selectedColor?.let { SettingsService.toColorHex(it) }
            ?: coverageColors.tracedColor
    }

    /**
     * Returns the Dialog panel for settings.
     *
     * @return the Dialog panel for settings
     */
    fun getComponent(): DialogPanel {
        return myPanel
    }
}
