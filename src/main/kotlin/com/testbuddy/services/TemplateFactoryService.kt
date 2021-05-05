package com.testbuddy.com.testbuddy.services

import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiMethod

class TemplateFactoryService(private val project: Project) {

    private val tm = TemplateManager.getInstance(project)

    /**
     * Creates a Template instance from a PSI method where the method identifier
     * is a variable to be replaced.
     *
     * @param psiMethod the PSI method to base the template off of
     */
    fun createTemplate(psiMethod: PsiMethod): Template {

        val template = tm.createTemplate("", "")

        val modifiers = psiMethod.modifierList
        val returnTy = psiMethod.returnType!!
        val identifier = psiMethod.name
        val params = psiMethod.parameterList
        val typeParameters = psiMethod.typeParameterList!!
        val throws = psiMethod.throwsList
        val code = psiMethod.body!!

        template.addTextSegment("${modifiers.text} ")
        template.addTextSegment(typeParameters.text)
        template.addTextSegment("${returnTy.canonicalText} ")
        template.addVariable("IDENTIFIER", ConstantNode(identifier), true)
        template.addTextSegment(params.text)
        template.addTextSegment(throws.text)
        template.addTextSegment(code.text)
        template.isToReformat = true

        return template
    }
}
