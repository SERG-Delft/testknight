package com.testbuddy.services

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.testbuddy.models.testingChecklist.TestingChecklist

@State(name = "ChecklistTreePersistent", storages = [Storage("checklistData.xml")])
class ChecklistTreePersistent : PersistentStateComponent<TestingChecklist> {

    private var testingChecklistTree: TestingChecklist = TestingChecklist(mutableListOf())

    /**
     * @return a component state. All properties, public and annotated fields are serialized. Only values, which differ
     * from the default (i.e., the value of newly instantiated class) are serialized. `null` value indicates
     * that the returned state won't be stored, as a result previously stored state will be used.
     * @see com.intellij.util.xmlb.XmlSerializer
     */
    override fun getState(): TestingChecklist {
        return testingChecklistTree
    }

    /**
     * This method is called when new component state is loaded. The method can and will be called several times, if
     * config files were externally changed while IDE was running.
     *
     *
     * State object should be used directly, defensive copying is not required.
     *
     * @param state loaded component state
     * @see com.intellij.util.xmlb.XmlSerializerUtil.copyBean
     */
    override fun loadState(state: TestingChecklist) {
        testingChecklistTree = state
    }

    companion object {
        val instance: ChecklistTreePersistent
            get() = ServiceManager.getService(ChecklistTreePersistent::class.java)
    }
}
