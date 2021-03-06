package com.testknight.services

import com.intellij.psi.PsiMethod
import com.testknight.extensions.TestKnightTestCase
import com.testknight.highlightResolutionStrategies.AssertionArgsStrategy
import junit.framework.TestCase
import org.junit.Test

internal class TemplateCreationServiceTest : TestKnightTestCase() {

    @Test
    fun testBasic() {
        var data = getBasicTestInfo("/Tests.java")

        val serv = TemplateCreationService(project)
        val methodToBeDuplicated = data.psiClass!!.findMethodsByName("basic")[0] as PsiMethod
        val template = serv.createBasicTemplate(methodToBeDuplicated)

        val expected = "@Test void (){\n" +
            "        // contents\n" +
            "    }"

        TestCase.assertEquals(expected, template.templateText)
    }

    @Test
    fun testReturnType() {
        var data = getBasicTestInfo("/Tests.java")

        val serv = TemplateCreationService(project)
        val methodToBeDuplicated = data.psiClass!!.findMethodsByName("hasReturnTy")[0] as PsiMethod
        val template = serv.createBasicTemplate(methodToBeDuplicated)

        val expected = "@Test String (){\n" +
            "        // contents\n" +
            "    }"

        TestCase.assertEquals(expected, template.templateText)
    }

    @Test
    fun testModifiers() {
        var data = getBasicTestInfo("/Tests.java")

        val serv = TemplateCreationService(project)
        val methodToBeDuplicated = data.psiClass!!.findMethodsByName("hasModifiers")[0] as PsiMethod
        val template = serv.createBasicTemplate(methodToBeDuplicated)

        val expected = "@Test\n" +
            "    public static void (){\n" +
            "        // contents\n" +
            "    }"

        TestCase.assertEquals(expected, template.templateText)
    }

    @Test
    fun testTypeParams() {
        var data = getBasicTestInfo("/Tests.java")

        val serv = TemplateCreationService(project)
        val methodToBeDuplicated = data.psiClass!!.findMethodsByName("hasTypeParams")[0] as PsiMethod
        val template = serv.createBasicTemplate(methodToBeDuplicated)

        val expected = "@Test <A, B>void (){\n" +
            "        // contents\n" +
            "    }"

        TestCase.assertEquals(expected, template.templateText)
    }

    @Test
    fun testThrows() {
        var data = getBasicTestInfo("/Tests.java")

        val serv = TemplateCreationService(project)
        val methodToBeDuplicated = data.psiClass!!.findMethodsByName("throwsException")[0] as PsiMethod
        val template = serv.createBasicTemplate(methodToBeDuplicated)

        val expected = "@Test void ()throws Exception{\n" +
            "        // contents\n" +
            "    }"

        TestCase.assertEquals(expected, template.templateText)
    }

    @Test
    fun testParams() {
        var data = getBasicTestInfo("/Tests.java")

        val serv = TemplateCreationService(project)

        val methodToBeDuplicated = data.psiClass!!.findMethodsByName("hasParams")[0] as PsiMethod
        val template = serv.createBasicTemplate(methodToBeDuplicated)

        val expected = "@Test void (int x, int y){\n" +
            "        // contents\n" +
            "        int expected = x;\n" +
            "        int actual = SomeClass.magic(x, y);\n" +
            "        assertEquals(expected, actual);\n" +
            "    }"

        TestCase.assertEquals(expected, template.templateText)
    }

    @Test
    fun testAdvanced() {
        var data = getBasicTestInfo("/Tests.java")

        val templateFactoryService = TemplateCreationService(project)
        val method = data.psiClass!!.findMethodsByName("hasAssertion")[0] as PsiMethod
        val psiElements = AssertionArgsStrategy.getElements(method)
        val template = templateFactoryService.createAdvancedTemplate(method, psiElements)

        val expected = "@Test void (){\n" +
            "        assertEquals(, )\n" +
            "    }"

        assertEquals(expected, template.templateText)
    }
}
