package com.testbuddy.settings

import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.ColorPanel
import com.intellij.ui.layout.panel

class SettingsComponent {

    var myPanel: DialogPanel
    private val state = SettingsService.instance.state

    init {
        myPanel = panel {
            titledRow("Telemetry") {
                row {
                    checkBox("Allow sending data", state.telemetrySettings::isEnabled)
                }
            }
            titledRow("Test List") {
                val testListSettings = state.testListSettings

                row {
                    checkBox("Auto update panel", testListSettings::autoUpdateUI)
                }
                row("Highlight strategies") {
                    for (i in testListSettings.highlightStrategies) {
                        row {
                            checkBox(i.key, { i.value }, { newVal -> i.setValue(newVal) })
                        }
                    }
                }
            }

            titledRow("Checklist") {
                val checklistSettings = state.checklistSettings
                val coverageComboBox = listOf("MCDC", "BRANCH")
                val savedSelection = checklistSettings.coverageCriteria
                val comboBoxModel = CollectionComboBoxModel(coverageComboBox, savedSelection)
                row {
                    label("Coverage Criteria")
                    comboBox(comboBoxModel, checklistSettings::coverageCriteria)
                }

                row {
                    checkBox("Show gutter icons", checklistSettings::showGutterIcons)
                }
                row {
                    checkBox("Goto checklist code", checklistSettings::gotoChecklistItem)
                }
                row {
                    checkBox("Highlight checklist code", checklistSettings::highlightChecklistItem)
                }

                row("Checklist strategies") {
                    for (i in checklistSettings.checklistStrategies) {
                        row {
                            checkBox(i.key, { i.value }, { newVal -> i.setValue(newVal) })
                        }
                    }
                }

                row("Datatype Map") {
                    for (i in checklistSettings.typeCaseMap) {
                        row {
                            label(i.key)
                        }
                    }
                }
            }

            titledRow("Coverage") {
                val coverageSettings = state.coverageSettings
                row {
                    val addedColor = ColorPanel()
                    addedColor.selectedColor = coverageSettings.includedColor

                    addedColor.addActionListener {
                        if (it.actionCommand == "colorPanelChanged" && addedColor.selectedColor != null) {
                            coverageSettings.deletedColor = addedColor.selectedColor!!
                        }
                    }

                    label("Recently added color: ")
                    addedColor()
                }
                row {
                    val deletedColor = ColorPanel()
                    deletedColor.selectedColor = coverageSettings.deletedColor

                    deletedColor.addActionListener {
                        if (it.actionCommand == "colorPanelChanged" && deletedColor.selectedColor != null) {
                            coverageSettings.deletedColor = deletedColor.selectedColor!!
                        }
                    }

                    label("Recently deleted color: ")
                    deletedColor()
                }
                row {
                    val addedColorDiff = ColorPanel()
                    addedColorDiff.selectedColor = coverageSettings.diffIncludedColor

                    addedColorDiff.addActionListener {
                        if (it.actionCommand == "colorPanelChanged" && addedColorDiff.selectedColor != null) {
                            coverageSettings.deletedColor = addedColorDiff.selectedColor!!
                        }
                    }

                    label("Recently added color in diff: ")
                    addedColorDiff()
                }
                row {
                    val deletedColorDiff = ColorPanel()
                    deletedColorDiff.selectedColor = coverageSettings.diffExcludedColor

                    deletedColorDiff.addActionListener {
                        if (it.actionCommand == "colorPanelChanged" && deletedColorDiff.selectedColor != null) {
                            coverageSettings.deletedColor = deletedColorDiff.selectedColor!!
                        }
                    }

                    label("Recently deleted color in diff: ")
                    deletedColorDiff()
                }
            }
        }
    }

    fun getComponent(): DialogPanel {
        return myPanel
    }
}
