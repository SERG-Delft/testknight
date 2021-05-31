package com.testbuddy.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.psi.PsiConditionalExpression
import com.intellij.psi.PsiDoWhileStatement
import com.intellij.psi.PsiForStatement
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiParameterList
import com.intellij.psi.PsiSwitchStatement
import com.intellij.psi.PsiThrowStatement
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.PsiWhileStatement
import com.intellij.ui.ColorUtil
import com.intellij.util.xmlb.XmlSerializerUtil
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.awt.Color

@State(name = "SettingsState", storages = [Storage("TestBuddySettings.xml")])
class SettingsService : PersistentStateComponent<SettingsState> {

    private var myState: SettingsState = SettingsState()

    /**
     * @return a component state. All properties, public and annotated fields are serialized. Only values, which differ
     * from the default (i.e., the value of newly instantiated class) are serialized. {@code null} value indicates
     * that the returned state won't be stored, as a result previously stored state will be used.
     * @see com.intellij.util.xmlb.XmlSerializer
     */
    @Nullable
    override fun getState(): SettingsState {
        return myState
    }

    /**
     * This method is called when new component state is loaded. The method can and will be called several times, if
     * config files were externally changed while IDE was running.
     * <p>
     * State object should be used directly, defensive copying is not required.
     *
     * @param stateLoadedFromPersistence loaded component state
     * @see com.intellij.util.xmlb.XmlSerializerUtil#copyBean(Object, Object)
     */
    override fun loadState(@NotNull stateLoadedFromPersistence: SettingsState) {
        XmlSerializerUtil.copyBean(stateLoadedFromPersistence, myState)
    }

    /**
     * Reset settings state to have the default settings.
     */
    fun resetState() {
        myState = SettingsState()
    }

    /**
     * Maps names in the settings panel to concrete Psi element types
     */
    var strategyNames = mapOf(
        "If Statement" to PsiIfStatement::class.java,
        "Switch Statement" to PsiSwitchStatement::class.java,
        "Try Statement" to PsiTryStatement::class.java,
        "Parameter List" to PsiParameterList::class.java,
        "While Statement" to PsiWhileStatement::class.java,
        "For Statement" to PsiForStatement::class.java,
        "Do While Statement" to PsiDoWhileStatement::class.java,
        "Foreach Statement" to PsiForeachStatement::class.java,
        "Throw Statement" to PsiThrowStatement::class.java,
        "Ternary Operator" to PsiConditionalExpression::class.java
    )

    companion object {
        val instance: SettingsService
            get() = ServiceManager.getService(SettingsService::class.java)

        /**
         * Converts colors from Hex form in String to Color object.
         */
        fun toColor(color: String): Color {
            return ColorUtil.fromHex(color)
        }

        /**
         * Converts colors from Color object to Hex form in String.
         */
        fun toColorHex(color: Color): String {
            return ColorUtil.toHex(color)
        }
    }
}
