package com.testbuddy.com.testbuddy.services

import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameterList

class TemplateFactoryService(private val project: Project) {

    private val tm = TemplateManager.getInstance(project)

    /**
     * Creates a Template instance from a PSI method where the method identifier
     * is a variable to be replaced.
     *
     * @param psiMethod the PSI method to base the template off of
     */
    fun createTemplate(psiMethod: PsiMethod): Template {

        // TODO throws List info
        // TODO static
        // TODO generics

        val template = tm.createTemplate("", "")

        val annotations: Array<PsiAnnotation> = psiMethod.annotations
        val returnTy = psiMethod.returnType
        val identifier = psiMethod.name
        val paramList: PsiParameterList = psiMethod.parameterList
        val code = psiMethod.body

        annotations.forEach { template.addTextSegment("${it.text}\n") }

        if (returnTy != null) {
            template.addTextSegment("${returnTy.canonicalText} ")
        }

        template.addVariable("IDENTIFIER", ConstantNode(identifier), true)

        template.addTextSegment(paramList.text)

        if (code != null) {
            template.addTextSegment(code.text)
        }

        template.isToReformat = true

        return template
    }
}
