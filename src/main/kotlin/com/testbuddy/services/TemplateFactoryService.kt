package com.testbuddy.com.testbuddy.services

import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameterList

class TemplateFactoryService(val project: Project) {

    val tm = TemplateManager.getInstance(project)

    /**
     * Creates a Template Instance from a PSI method
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

        annotations.forEach { it -> template.addTextSegment("${it.text}\n") }

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
