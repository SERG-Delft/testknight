package com.testbuddy.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.PsiClass
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies.ClassChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies.MethodChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistClassNode
import com.testbuddy.com.testbuddy.models.TestingChecklistMethodNode
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
            emptyList<TestingChecklistMethodNode>(),
            testClass!!
        )
        val actual = generator.generateChecklist(testClass)
        TestCase.assertEquals(expected, actual)
    }
}
