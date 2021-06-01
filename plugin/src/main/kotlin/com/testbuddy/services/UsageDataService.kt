package com.testbuddy.services

import com.intellij.openapi.components.ServiceManager
import com.testbuddy.models.ActionData

@Suppress("TooManyFunctions")
class UsageDataService {

    private val actions = mutableListOf<ActionData>()

    // a set of functions to log actions

    fun logDuplicateTest() = actions.add(ActionData("duplicateTest"))

    fun logGotoTest() = actions.add(ActionData("gotoTest"))

    fun logAssertionSuggestion() = actions.add(ActionData("suggestAssertion"))

    fun logGenerateChecklist() = actions.add(ActionData("generateChecklist"))

    fun logSplitDiffView() = actions.add(ActionData("splitDiffView"))

    fun logIntegratedDiffView() = actions.add(ActionData("integratedDiffView"))

    fun logTraceTest() = actions.add(ActionData("traceTest"))

    fun logGenerateTest() = actions.add(ActionData("generateTest"))

    fun logItemMarked() = actions.add(ActionData("itemMarked"))

    fun logItemDeleted() = actions.add(ActionData("itemDeleted"))

    fun logRunWithCoverage() = actions.add(ActionData("runWithCoverage"))

    fun logTestRun() = actions.add(ActionData("testRun"))

    fun logTestFail() = actions.add(ActionData("testFail"))

    fun logTestAdd() = actions.add(ActionData("testAdd"))

    companion object {
        val instance: UsageDataService
            get() = ServiceManager.getService(UsageDataService::class.java)
    }
}
