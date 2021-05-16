package com.testbuddy.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.PsiElement
import com.testbuddy.checklistGenerationStrategies.ChecklistGeneratorStrategy
import com.testbuddy.models.TestingChecklistParentNode

interface ParentChecklistGeneratorStrategy<
    E : PsiElement,
    G : TestingChecklistParentNode> : ChecklistGeneratorStrategy<E, G>
