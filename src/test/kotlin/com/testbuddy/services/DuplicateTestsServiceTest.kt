package com.testbuddy.services

import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase
import org.junit.Before
import org.junit.jupiter.api.Test

class DuplicateTestsServiceTest : BasePlatformTestCase() {

    private val service = DuplicateTestsService()

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun test() {
        this.myFixture.configureByFile("/PointTest.java")
        val psi = this.myFixture.file
        val editor = this.myFixture.editor
        val project = this.myFixture.project
        editor.caretModel.primaryCaret.moveToOffset(600)
        service.addToPsi(psi, editor, project)
        val testMethod = PsiTreeUtil.findChildrenOfType(psi, PsiMethod::class.java)
        TestCase.assertEquals("PsiMethod:translateTest", testMethod.lastOrNull().toString())
    }
}
