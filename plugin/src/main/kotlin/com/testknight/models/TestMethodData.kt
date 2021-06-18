package com.testknight.models

import com.intellij.psi.PsiMethod

/**
 * The TestMethodData class contains information about a test method.
 *
 * @param name the name of the test method.
 * @param testClassName the name of the class the method is part of.
 *                      If for some reason the method is not part of a class this will be the empty string.
 * @param psiMethod the reference to the actual method as part of the PsiTree.
 */
data class TestMethodData(val name: String, val testClassName: String, val psiMethod: PsiMethod)
