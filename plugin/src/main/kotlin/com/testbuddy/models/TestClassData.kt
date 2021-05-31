package com.testbuddy.models

import com.intellij.psi.PsiClass

/**
 * Contains information about a test class.
 *
 * @param name the name of the test class
 * @param methods list of TestMethodData objects representing the methods in the class
 * @param psiClass the reference to the actual class in the PSI tree
 */
data class TestClassData(val name: String, val methods: List<TestMethodData>, val psiClass: PsiClass)
