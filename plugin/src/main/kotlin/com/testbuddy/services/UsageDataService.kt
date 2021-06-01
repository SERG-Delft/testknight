package com.testbuddy.services

import com.intellij.openapi.components.ServiceManager
import com.testbuddy.models.ActionData
import com.testbuddy.models.UsageData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Suppress("TooManyFunctions")
class UsageDataService {

    private val actionsRecorded = mutableListOf<ActionData>()

    // a set of functions to log actions

    private fun log(actionId: String) {
        actionsRecorded.add(ActionData(actionId))
        println("Action $actionId has been executed")
    }

    fun logDuplicateTest() = log("duplicateTest")

    fun logGotoTest() = log("gotoTest")

    fun logAssertionSuggestion() = log("suggestAssertion")

    fun logGenerateChecklist() = log("generateChecklist")

    fun logSplitDiffView() = log("splitDiffView")

    fun logIntegratedDiffView() = log("integratedDiffView")

    fun logTraceTest() = log("traceTest")

    fun logGenerateTest() = log("generateTest")

    fun logItemMarked() = log("itemMarked")

    fun logItemDeleted() = log("itemDeleted")

    fun logRunWithCoverage() = log("runWithCoverage")

    fun logTestRun() = log("testRun")

    fun logTestFail() = log("testFail")

    fun logTestAdd() = log("testAdd")

    /**
     * @return the current usage data.
     */
    fun usageDataJson() = Json.encodeToString(UsageData(actionsRecorded))

    companion object {
        val instance: UsageDataService
            get() = ServiceManager.getService(UsageDataService::class.java)
    }
}
