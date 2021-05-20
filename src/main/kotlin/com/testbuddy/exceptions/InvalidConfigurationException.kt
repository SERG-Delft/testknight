package com.testbuddy.com.testbuddy.exceptions

class InvalidConfigurationException(private val property: String, private val invalidConfiguration: String) :
    Exception("Property $property cannot be set to $invalidConfiguration")
