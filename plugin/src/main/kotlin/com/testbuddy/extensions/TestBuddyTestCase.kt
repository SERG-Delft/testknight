package com.testbuddy.extensions

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.settings.SettingsService
import org.jetbrains.annotations.NotNull

open class TestBuddyTestCase : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        SettingsService.instance.resetState()
    }

    override fun getTestDataPath() = "testdata"

    fun getBasicTestInfo(filepath: String): Data {
        myFixture.configureByFile(filepath)
        val project = myFixture.project
        val psiClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)
        val psiFile = myFixture.file
        val testClasses = PsiTreeUtil.findChildrenOfType(psiFile, PsiClass::class.java)
        val editor = myFixture.editor
        return Data(filepath, project, psiClass, psiFile, testClasses, editor)
    }

    fun getMethodByName(methodName: String): PsiMethod {
        val psi = this.myFixture.file
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        return testClass!!.findMethodsByName(methodName)[0] as PsiMethod
    }
}

data class Data(
        var filepath: String,
        var project: Project,
        var psiClass: PsiClass?,
        var psiFile: PsiFile,
        val testClasses: @NotNull MutableCollection<PsiClass>,
        val editor: Editor
)
