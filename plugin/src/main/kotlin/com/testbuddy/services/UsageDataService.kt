package com.testbuddy.services

import com.testbuddy.exceptions.InvalidActionIdException
import com.testbuddy.models.ActionData
import com.testbuddy.models.UsageData
import java.util.UUID

class UsageDataService {

    val userId = UUID.randomUUID()

    private val actionIds = setOf(
        "duplicateTest",
        "gotoTest",
        "suggestAssertion",
        "generateChecklist",
        "generateTestSkeleton",
        "splitDiffView",
        "integratedDiffView",
        "traceTest",
        "itemMarked",
        "runWithCoverage",
        "testRun",
        "testFail",
        "testAdd"
    )

    private val actions = mutableListOf<ActionData>()

    /**
     * Logs an action given an actionId
     *
     * @param actionId the action ID
     * @throws InvalidActionIdException
     */
    @Throws(InvalidActionIdException::class)
    fun logAction(actionId: String) = if (actionIds.contains(actionId)) {
        actions.add(ActionData(actionId))
    } else {
        throw InvalidActionIdException(actionId)
    }

    fun constructUsageData(): UsageData {
        return UsageData(actions)
    }
}
