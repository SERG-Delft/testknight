package com.testbuddy.exceptions

class InvalidVirtualFileException(className: String, invalidReason: String) :
    Exception("Invalid virtual file for class $className. Reason: $invalidReason")
