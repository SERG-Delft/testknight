package com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiElement
import com.testbuddy.checklistGenerationStrategies.ChecklistGeneratorStrategy
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode

interface LeafChecklistGeneratorStrategy<E : PsiElement> : ChecklistGeneratorStrategy<E, List<TestingChecklistLeafNode>>
