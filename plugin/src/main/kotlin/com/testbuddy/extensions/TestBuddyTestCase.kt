package com.testbuddy.extensions

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

    fun getBasicTestInfo(filepath: String) {
        myFixture.configureByFile(filepath)
        val project = myFixture.project
        val psiClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)
        val psiFile = myFixture.file
        val testClasses = PsiTreeUtil.findChildrenOfType(psiFile, PsiClass::class.java)
    }
}
