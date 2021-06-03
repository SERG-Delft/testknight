package com.testbuddy.models.testingChecklist.parentNodes

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.util.xmlb.annotations.OptionTag
import com.intellij.util.xmlb.annotations.XCollection
import com.testbuddy.models.testingChecklist.TestingChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.ConditionChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.CustomChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.ParameterChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testbuddy.models.testingChecklist.leafNodes.ThrowStatementChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.branchingStatements.SwitchStatementChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.branchingStatements.TryStatementChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.loopStatements.DoWhileStatementChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.loopStatements.ForEachStatementChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.loopStatements.ForLoopStatementChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.loopStatements.WhileStatementChecklistNode
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

    @XCollection(
        elementTypes =
        [
            ThrowStatementChecklistNode::class,
            ParameterChecklistNode::class,
            CustomChecklistNode::class,
            ConditionChecklistNode::class,
            WhileStatementChecklistNode::class,
            ForLoopStatementChecklistNode::class,
            ForEachStatementChecklistNode::class,
            DoWhileStatementChecklistNode::class,
            TryStatementChecklistNode::class,
            SwitchStatementChecklistNode::class
        ]
    )
    var children: MutableList<TestingChecklistLeafNode> = mutableListOf(),
    @OptionTag(converter = PsiConverter::class)
    var element: PsiMethod? = null,
) : TestingChecklistParentNode()
