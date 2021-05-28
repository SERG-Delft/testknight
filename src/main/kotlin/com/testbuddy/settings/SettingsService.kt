package com.testbuddy.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

@State(name = "com.testbuddy.settings.SettingsState", storages = [Storage("TestBuddySettings.xml")])
class SettingsService : PersistentStateComponent<SettingsState> {

    private var myState: SettingsState = SettingsState()

    @Nullable
    override fun getState(): SettingsState {
        return myState
    }

    override fun loadState(@NotNull stateLoadedFromPersistence: SettingsState) {
        myState = stateLoadedFromPersistence
    }

    companion object {
        val instance: SettingsService
            get() = ServiceManager.getService(SettingsService::class.java)
    }
}
