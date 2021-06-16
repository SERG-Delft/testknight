package com.testknight.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiElement
import com.testknight.checklistGenerationStrategies.ChecklistGeneratorStrategy
import com.testknight.models.testingChecklist.leafNodes.TestingChecklistLeafNode

interface LeafChecklistGeneratorStrategy<E : PsiElement> : ChecklistGeneratorStrategy<E, List<TestingChecklistLeafNode>>
