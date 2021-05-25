package com.testbuddy.settings

data class SettingsState(var myFlag : Boolean = false,
                         var myString: String = "",
                         var myInt: Int = 0,
                         var myStringChoice: String = "") {

    fun copyTo(copyTo : SettingsState) {
        copyTo.myFlag = this.myFlag
        copyTo.myString = this.myString
        copyTo.myInt = this.myInt
        copyTo.myStringChoice = this.myStringChoice
    }
}
