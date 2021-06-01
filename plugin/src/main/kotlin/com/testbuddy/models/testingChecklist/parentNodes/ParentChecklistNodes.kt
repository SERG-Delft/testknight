package com.testbuddy.models.testingChecklist.parentNodes

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.util.xmlb.annotations.OptionTag
import com.testbuddy.models.testingChecklist.TestingChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testbuddy.utilities.PsiConverter

abstract class TestingChecklistParentNode : TestingChecklistNode()

data class TestingChecklistClassNode(
    var description: String = "",
    var children: MutableList<TestingChecklistMethodNode> = mutableListOf(),
    @OptionTag(converter = PsiConverter::class)
    var element: PsiClass? = null,
) : TestingChecklistParentNode()

data class TestingChecklistMethodNode(
    var description: String = "",
    var children: MutableList<TestingChecklistLeafNode> = mutableListOf(),
    @OptionTag(converter = PsiConverter::class)
    var element: PsiMethod? = null,
) : TestingChecklistParentNode()
