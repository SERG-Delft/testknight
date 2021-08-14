package com.testknight.checklistGenerationStrategies

import com.testknight.extensions.TestKnightTestCase
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistClassNode
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import org.junit.jupiter.api.Test

internal class TestKnightJavaPsiVisitorTest : TestKnightTestCase() {

    @Test
    fun testClassInitializedProperly() {
        val visitor = TestKnightJavaPsiVisitor()
        val data = getBasicTestInfo("/EmptyClass.java")
        data.psiClass?.accept(visitor)
        val expected = TestingChecklistClassNode("EmptyClass", mutableListOf(), data.psiClass)
        val actual = visitor.classNode
        assertEquals(expected, actual)
    }

    @Test
    fun testMethodsAreNotAnalyzed() {
        val visitor = TestKnightJavaPsiVisitor()
        val data = getBasicTestInfo("/Tests.java")
        data.psiClass?.accept(visitor)
        val expected = TestingChecklistClassNode(
            "PointTest",
            mutableListOf(
                TestingChecklistMethodNode("basic", mutableListOf(), data.psiClass?.methods?.get(0)),
                TestingChecklistMethodNode("hasModifiers", mutableListOf(), data.psiClass?.methods?.get(1)),
                TestingChecklistMethodNode("hasReturnTy", mutableListOf(), data.psiClass?.methods?.get(2)),
                TestingChecklistMethodNode("hasParams", mutableListOf(), data.psiClass?.methods?.get(3)),
                TestingChecklistMethodNode("hasTypeParams", mutableListOf(), data.psiClass?.methods?.get(4)),
                TestingChecklistMethodNode("throwsException", mutableListOf(), data.psiClass?.methods?.get(5)),
                TestingChecklistMethodNode("hasAssertion", mutableListOf(), data.psiClass?.methods?.get(6))
            ),
            data.psiClass
        )
        val actual = visitor.classNode
        assertEquals(expected, actual)
    }
}
