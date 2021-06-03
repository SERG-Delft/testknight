package com.testbuddy.exceptions

class InvalidActionIdException(actionId: String) : Exception("$actionId is not a valid action ID")
