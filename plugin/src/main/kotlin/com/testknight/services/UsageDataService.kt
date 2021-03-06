package com.testknight.services

import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.core.isClientError
import com.github.kittinunf.fuel.core.isServerError
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import com.intellij.execution.testframework.AbstractTestProxy
import com.intellij.openapi.components.ServiceManager
import com.testknight.messageBundleHandlers.ServerMessageBundleHandler
import com.testknight.models.ActionData
import com.testknight.models.UsageData
import com.testknight.settings.SettingsService

@Suppress("TooManyFunctions")
class UsageDataService {

    /**
     * List of recorded actions.
     */
    private var actionsRecorded = mutableListOf<ActionData>()

    /**
     * The amount of actions that are stored before sending them
     */
    @Suppress("MagicNumber")
    private val actionsThreshold = 150

    /**
     * A set of hashes of the known tests. Used to detect new tests.
     */
    private val knownTests = HashSet<Int>()

    private val serverUrl = ServerMessageBundleHandler.message("debugUrl")

    private fun telemetryEnabled() = SettingsService.instance.state.telemetrySettings.isEnabled

    /**
     * Add the action with the provided actionId to the
     * recorded actions. Only runs if telemetry is enabled
     *
     * @param actionId the action id
     */
    private fun record(actionId: String): ActionData? {
        if (telemetryEnabled()) {
            val action = ActionData(actionId)
            actionsRecorded.add(action)
            println("Action $actionId has been executed (${actionsRecorded.size}/50)")

            // periodically send the usage data
            if (actionsRecorded.size >= actionsThreshold) {
                sendUserData()
                clearRecords()
            }
            return action
        }
        return null
    }

    // a set of functions to log actions

    fun recordDuplicateTest() = record("duplicateTest")

    fun recordGotoTest() = record("gotoTest")

    fun recordSuggestAssertion() = record("suggestAssertion")

    fun recordGenerateChecklist() = record("generateChecklist")

    fun recordSplitDiffView() = record("splitDiffView")

    fun recordIntegratedDiffView() = record("integratedDiffView")

    fun recordTraceTest() = record("traceTest")

    fun recordGenerateTest() = record("generateTest")

    fun recordItemMarked() = record("itemMarked")

    fun recordItemDeleted() = record("itemDeleted")

    fun recordRunWithCoverage() = record("runWithCoverage")

    private fun recordTestRun() = record("testRun")

    private fun recordTestFail() = record("testFail")

    private fun recordTestAdd() = record("testAdd")

    /**
     * For each test check if it is known, if
     * not mark it as known and log a test add.
     *
     * @param root the test proxy
     */
    fun logTests(root: AbstractTestProxy) {
        for (test in root.allTests) {
            if (test.isLeaf) {

                // log test runs
                recordTestRun()

                // log test failures
                if (!test.isPassed) recordTestFail()

                val hash = "${test.locationUrl}${test.name}".hashCode()

                if (!knownTests.contains(hash)) {
                    instance.recordTestAdd()
                    knownTests.add(hash)
                }
            }
        }
    }

    /**
     * Clear all usage data
     */
    fun clearRecords() {
        actionsRecorded = mutableListOf()
    }

    /**
     * Get the usage data in JSON format.
     *
     * @return the current usage data.
     */
    fun usageDataJson() = Gson().toJson(usageData())

    /**
     * Get the usage data instance.
     *
     * @return the UsageData instance
     */
    fun usageData() = UsageData(actionsRecorded)

    /**
     * Send the user data to the server in the form of an HTTP Post request
     */
    fun sendUserData() {
        if (telemetryEnabled()) {

            val (req, result, response) = ServerMessageBundleHandler.message("usageData", serverUrl)
                .httpPost()
                .jsonBody(usageDataJson())
                .responseString()

            when {
                result.isSuccessful -> println("Usage Data sent successfully")
                result.isClientError ->
                    println("Failed to send usage data: client error: ${result.statusCode} ${result.responseMessage}")
                result.isServerError ->
                    println("Failed to send usage data: server error: ${result.statusCode} ${result.responseMessage}")
                else -> println("Failed to send usage data: ${result.statusCode} ${result.responseMessage}")
            }
        }
    }

    companion object {
        val instance: UsageDataService
            get() = ServiceManager.getService(UsageDataService::class.java)
    }
}
