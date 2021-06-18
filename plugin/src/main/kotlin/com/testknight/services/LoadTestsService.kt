package com.testknight.services

import com.intellij.openapi.Disposable
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testknight.models.TestClassData
import com.testknight.models.TestMethodData

class LoadTestsService : Disposable {

    private val testAnalyzer = TestAnalyzerService()

    /**
     * Extracts all the test methods from a PSI file.
     *
     * @param file the PSI file
     * @return a list of TestCaseData elements representing the test cases
     */
    fun getTests(file: PsiFile): List<TestMethodData> {
        val methods = PsiTreeUtil.findChildrenOfType(file, PsiMethod::class.java)
        return methods.filter { testAnalyzer.isTestMethod(it) }.map {
            TestMethodData(it.name, (it.parent as PsiClass).name ?: "", it)
        }
    }

    /**
     * Extract all test methods in a PSI file in the form of a tree.
     *
     * @param file the PSI file
     * @return a list of TestClassData corresponding to the classes in the file
     */
    fun getTestsTree(file: PsiFile): List<TestClassData> {
        val classes = PsiTreeUtil.findChildrenOfType(file, PsiClass::class.java)

        return classes
            .filter { testAnalyzer.isTestClass(it) }
            .map { psiClass ->
                TestClassData(
                    psiClass.name ?: "",
                    psiClass.methods
                        .filter { testAnalyzer.isTestMethod(it) }
                        .map { TestMethodData(it.name, psiClass.name ?: "", it) },
                    psiClass
                )
            }
    }

    /**
     * Overridden function for Disposable. Doesn't require anything to be disposed.
     */
    override fun dispose() {
        // No specific dispose function is required.
    }
}
