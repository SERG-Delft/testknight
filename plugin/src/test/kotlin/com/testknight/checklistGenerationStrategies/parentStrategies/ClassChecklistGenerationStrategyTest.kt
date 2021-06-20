package com.testknight.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testknight.extensions.TestKnightTestCase
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistClassNode
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Test

internal class ClassChecklistGenerationStrategyTest : TestKnightTestCase() {

    @Test
    fun testEmptyClass() {
        val data = getBasicTestInfo("/EmptyClass.java")
        val generator = ClassChecklistGenerationStrategy.create(MethodChecklistGenerationStrategy.create())

        val expected = TestingChecklistClassNode(
            "EmtpyClass",
            mutableListOf<TestingChecklistMethodNode>(),
            data.psiClass!!
        )
        val actual = generator.generateChecklist(data.psiClass!!)

        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testClassChecklist() {
        val data = getBasicTestInfo("/Math.java")
        val methodChecklistGenerationStrategy = mockk<MethodChecklistGenerationStrategy>()

        val methodsInClass = PsiTreeUtil.findChildrenOfType(data.psiClass, PsiMethod::class.java)

        every { methodChecklistGenerationStrategy.generateChecklist(methodsInClass.elementAt(0)) } returns
            TestingChecklistMethodNode("add", mutableListOf(), methodsInClass.elementAt(0))
        every { methodChecklistGenerationStrategy.generateChecklist(methodsInClass.elementAt(1)) } returns
            TestingChecklistMethodNode("sub", mutableListOf(), methodsInClass.elementAt(1))
        every { methodChecklistGenerationStrategy.generateChecklist(methodsInClass.elementAt(2)) } returns
            TestingChecklistMethodNode("mult", mutableListOf(), methodsInClass.elementAt(2))
        every { methodChecklistGenerationStrategy.generateChecklist(methodsInClass.elementAt(3)) } returns
            TestingChecklistMethodNode("div", mutableListOf(), methodsInClass.elementAt(3))

        val expectedChildren = mutableListOf(
            TestingChecklistMethodNode("add", mutableListOf(), methodsInClass.elementAt(0)),
            TestingChecklistMethodNode("sub", mutableListOf(), methodsInClass.elementAt(1)),
            TestingChecklistMethodNode("mult", mutableListOf(), methodsInClass.elementAt(2)),
            TestingChecklistMethodNode("div", mutableListOf(), methodsInClass.elementAt(3))
        )

        val expected = TestingChecklistClassNode(
            "Math",
            expectedChildren,
            data.psiClass!!
        )
        val result =
            ClassChecklistGenerationStrategy.create(methodChecklistGenerationStrategy).generateChecklist(data.psiClass!!)

        TestCase.assertEquals(expected, result)
    }
}
