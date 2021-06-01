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

    fun logDuplicateTest() = actionsRecorded.add(ActionData("duplicateTest"))

    fun logGotoTest() = actionsRecorded.add(ActionData("gotoTest"))

    fun logAssertionSuggestion() = actionsRecorded.add(ActionData("suggestAssertion"))

    fun logGenerateChecklist() = actionsRecorded.add(ActionData("generateChecklist"))

    fun logSplitDiffView() = actionsRecorded.add(ActionData("splitDiffView"))

    fun logIntegratedDiffView() = actionsRecorded.add(ActionData("integratedDiffView"))

    fun logTraceTest() = actionsRecorded.add(ActionData("traceTest"))

    fun logGenerateTest() = actionsRecorded.add(ActionData("generateTest"))

    fun logItemMarked() = actionsRecorded.add(ActionData("itemMarked"))

    fun logItemDeleted() = actionsRecorded.add(ActionData("itemDeleted"))

    fun logRunWithCoverage() = actionsRecorded.add(ActionData("runWithCoverage"))

    fun logTestRun() = actionsRecorded.add(ActionData("testRun"))

    fun logTestFail() = actionsRecorded.add(ActionData("testFail"))

    fun logTestAdd() = actionsRecorded.add(ActionData("testAdd"))

    /**
     * @return the current usage data.
     */
    fun usageDataJson() = Json.encodeToString(UsageData(actionsRecorded))

    companion object {
        val instance: UsageDataService
            get() = ServiceManager.getService(UsageDataService::class.java)
    }
}
