package com.testbuddy.services

import com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies.ClassChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies.MethodChecklistGenerationStrategy

class GenerateTestCaseChecklistService {

    var classChecklistGenerationStrategy = ClassChecklistGenerationStrategy.create()
    var methodChecklistGenerationStrategy = MethodChecklistGenerationStrategy.create()

}
