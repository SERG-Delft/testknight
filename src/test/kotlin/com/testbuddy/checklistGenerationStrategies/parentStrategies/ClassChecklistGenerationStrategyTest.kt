package com.testbuddy.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistClassNode
import com.testbuddy.com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class ClassChecklistGenerationStrategyTest : BasePlatformTestCase() {

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testEmptyClass() {
        val generator = ClassChecklistGenerationStrategy.create(MethodChecklistGenerationStrategy.create())
        this.myFixture.configureByFile("/EmptyClass.java")
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val expected = TestingChecklistClassNode(
            "EmtpyClass",
            mutableListOf<TestingChecklistMethodNode>(),
            testClass!!
        )
        val actual = generator.generateChecklist(testClass)
        TestCase.assertEquals(expected, actual)
    }

    @Test
    fun testClassChecklist() {
        val methodChecklistGenerationStrategy = mockk<MethodChecklistGenerationStrategy>()

        this.myFixture.configureByFile("/Math.java")
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val methodsInClass = PsiTreeUtil.findChildrenOfType(testClass, PsiMethod::class.java)

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
            testClass!!
        )

        val result =
            ClassChecklistGenerationStrategy.create(methodChecklistGenerationStrategy).generateChecklist(testClass)
        TestCase.assertEquals(expected, result)
    }
}
