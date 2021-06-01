package com.testbuddy.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ActionData(actionId: String) {

    val datetime: String = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())
}
