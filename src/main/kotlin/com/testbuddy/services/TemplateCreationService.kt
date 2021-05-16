package com.testbuddy.services

import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset

class TemplateCreationService(private val project: Project) {

    private val templateManager = TemplateManager.getInstance(project)

    /**
     * Creates a template for a method signature from a PsiMethod. The identifier is a variable to be replaced.
     *
     * @param psiMethod the PSI method
     * @return a template for the method signature extracted from the provided PsiMethod
     */
    private fun createSignatureTemplate(psiMethod: PsiMethod): Template {

        val template = templateManager.createTemplate("", "")

        val modifiers = psiMethod.modifierList
        val returnTy = psiMethod.returnType!!
        val identifier = psiMethod.name
        val params = psiMethod.parameterList
        val typeParameters = psiMethod.typeParameterList!!
        val throws = psiMethod.throwsList

        template.addTextSegment("${modifiers.text} ")
        template.addTextSegment(typeParameters.text)
        template.addTextSegment("${returnTy.canonicalText} ")
        template.addVariable("IDENTIFIER", ConstantNode(identifier), true)
        template.addTextSegment(params.text)
        template.addTextSegment(throws.text)
        template.isToReformat = true

        return template
    }

    /**
     * Creates a Template from a PSI method where the method identifier
     * is a variable to be replaced.
     *
     * @param psiMethod the PSI method to base the template off of
     */
    fun createBasicTemplate(psiMethod: PsiMethod): Template {

        val template = createSignatureTemplate(psiMethod)

        val code = psiMethod.body!!
        template.addTextSegment(code.text)

        return template
    }

    /**
     * Creates a Template from a PSI method where the method identifier
     * and of the provided PsiElements are variables to be replaced.
     *
     * @param psiMethod the PSI method to base the template off of
     * @param psiElements a list of PsiElements to be replaced
     */
    fun createAdvancedTemplate(psiMethod: PsiMethod, psiElements: List<PsiElement>): Template {

        // if the list of psiElements provided is empty just treat it like a basicTemplate
        if (psiElements.isEmpty()) {
            return createBasicTemplate(psiMethod)
        }

        val template = createSignatureTemplate(psiMethod)
        val code = psiMethod.body!!

        val startSubstring = code.text.substring(0, psiElements[0].startOffset - code.startOffset)
        template.addTextSegment(startSubstring)

        for (i in 0..psiElements.size - 2) {
            template.addVariable("CHG$i", ConstantNode(psiElements[i].text), true)

            val start = psiElements[i].endOffset - code.startOffset
            val end = psiElements[i + 1].startOffset - code.startOffset

            val substring = code.text.substring(start, end)
            template.addTextSegment(substring)
        }

        template.addVariable("CHG${psiElements.lastIndex}", ConstantNode(psiElements.last().text), true)
        val endSubstring = code.text.substring(psiElements.last().endOffset - code.startOffset, code.text.length)
        template.addTextSegment(endSubstring)

        return template
    }
}
