package com.testknight.models.testingChecklist.parentNodes

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.util.xmlb.annotations.OptionTag
import com.intellij.util.xmlb.annotations.XCollection
import com.testknight.models.testingChecklist.TestingChecklistNode
import com.testknight.models.testingChecklist.leafNodes.ConditionChecklistNode
import com.testknight.models.testingChecklist.leafNodes.CustomChecklistNode
import com.testknight.models.testingChecklist.leafNodes.ParameterChecklistNode
import com.testknight.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testknight.models.testingChecklist.leafNodes.ThrowStatementChecklistNode
import com.testknight.models.testingChecklist.leafNodes.branchingStatements.SwitchStatementChecklistNode
import com.testknight.models.testingChecklist.leafNodes.branchingStatements.TryStatementChecklistNode
import com.testknight.models.testingChecklist.leafNodes.loopStatements.DoWhileStatementChecklistNode
import com.testknight.models.testingChecklist.leafNodes.loopStatements.ForEachStatementChecklistNode
import com.testknight.models.testingChecklist.leafNodes.loopStatements.ForLoopStatementChecklistNode
import com.testknight.models.testingChecklist.leafNodes.loopStatements.WhileStatementChecklistNode
import com.testknight.utilities.PsiConverter

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
