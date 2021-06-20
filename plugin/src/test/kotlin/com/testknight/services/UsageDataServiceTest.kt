package com.testknight.services

import com.google.gson.Gson
import com.testknight.extensions.TestKnightTestCase
import com.testknight.models.ActionData
import com.testknight.models.UsageData
import com.testknight.settings.SettingsService
import junit.framework.TestCase
import org.junit.Test

class UsageDataServiceTest : TestKnightTestCase() {

    override fun setUp() {
        super.setUp()
        UsageDataService.instance.clearRecords()
    }

    @Test
    private fun recordActions(): List<ActionData>? {
        val a0 = UsageDataService.instance.recordDuplicateTest()
        val a1 = UsageDataService.instance.recordGotoTest()
        val a2 = UsageDataService.instance.recordSuggestAssertion()
        val a3 = UsageDataService.instance.recordGenerateChecklist()
        val a4 = UsageDataService.instance.recordSplitDiffView()
        val a5 = UsageDataService.instance.recordIntegratedDiffView()
        val a6 = UsageDataService.instance.recordTraceTest()
        val a7 = UsageDataService.instance.recordGenerateTest()
        val a8 = UsageDataService.instance.recordItemMarked()
        val a9 = UsageDataService.instance.recordItemDeleted()

        val actions = listOf(a0, a1, a2, a3, a4, a5, a6, a7, a8, a9)
        actions.forEach { it ?: return null }

        return actions.map { it!! }
    }

    @Test
    fun testBasic() {
        SettingsService.instance.state.telemetrySettings.isEnabled = true

        val actions = recordActions()!!

        val expected = UsageData(actions)
        val actual = UsageDataService.instance.usageData()

        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testJson() {
        SettingsService.instance.state.telemetrySettings.isEnabled = true

        val actions = recordActions()!!

        val expected = Gson().toJson(UsageData(actions))
        val actual = UsageDataService.instance.usageDataJson()

        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testTelemetryDisabled() {

        // telemetry should be false by default

        recordActions()

        val expected = UsageData(listOf())
        val actual = UsageDataService.instance.usageData()

        assertEquals(expected, actual)
    }
}
