package com.testknight.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.PsiElement
import com.testknight.checklistGenerationStrategies.ChecklistGeneratorStrategy
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistParentNode

interface ParentChecklistGeneratorStrategy<
    E : PsiElement,
    G : TestingChecklistParentNode> : ChecklistGeneratorStrategy<E, G>
