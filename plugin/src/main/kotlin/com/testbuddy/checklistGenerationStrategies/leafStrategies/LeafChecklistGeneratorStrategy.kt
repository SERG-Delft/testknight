package com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiElement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.ChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode

interface LeafChecklistGeneratorStrategy<E : PsiElement> : ChecklistGeneratorStrategy<E, List<TestingChecklistLeafNode>>
