package com.testbuddy.services

import com.google.gson.Gson
import com.intellij.execution.testframework.AbstractTestProxy
import com.intellij.openapi.components.ServiceManager
import com.testbuddy.models.ActionData
import com.testbuddy.models.UsageData
import com.testbuddy.settings.SettingsService

@Suppress("TooManyFunctions")
class UsageDataService {

    /**
     * List of recorded actions.
     */
    private val actionsRecorded = mutableListOf<ActionData>()

    /**
     * A set of hashes of the known tests. Used to detect new tests.
     */
    private val knownTests = HashSet<Int>()

    /**
     * Add the action with the provided actionId to the log. Only runs if telemetry is enabled
     *
     * @param actionId the action id
     */
    private fun log(actionId: String) {
        if (SettingsService.instance.state.telemetrySettings.isEnabled) {
            actionsRecorded.add(ActionData(actionId))
            println("Action $actionId has been executed")
        }
    }

    // a set of functions to log actions

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
     * For each test check if it is known, if not mark it as known and log a test add.
     *
     * @param root the test proxy
     */
    fun logTests(root: AbstractTestProxy) {
        for (test in root.allTests) {
            if (test.isLeaf) {

                // log test runs
                logTestRun()

                // log test failures
                if (!test.isPassed) logTestFail()

                val hash = "${test.locationUrl}${test.name}".hashCode()

                if (!knownTests.contains(hash)) {
                    instance.logTestAdd()
                    knownTests.add(hash)
                }
            }
        }
    }

    /**
     * Get the usage data in JSON format.
     *
     * @return the current usage data.
     */
    fun usageDataJson() = Gson().toJson(UsageData(actionsRecorded))

    fun usageData() = UsageData(actionsRecorded)

    companion object {
        val instance: UsageDataService
            get() = ServiceManager.getService(UsageDataService::class.java)
    }
}
