package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.com.testbuddy.services.TemplateFactoryService
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

internal class TemplateFactoryServiceTest : BasePlatformTestCase() {

    @Before
    public override fun setUp() {
        super.setUp()

        this.myFixture.configureByFile("/Tests.java")
        val psi = this.myFixture.file
        val editor = this.myFixture.editor
        val project = this.myFixture.project
        val testClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)!!
        val serv = TemplateFactoryService(project)
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testBasic() {

        this.myFixture.configureByFile("/Tests.java")
        val serv = TemplateFactoryService(project)

        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        val methodToBeDuplicated = testClass.findMethodsByName("basic")[0] as PsiMethod
        val template = serv.createTemplate(methodToBeDuplicated)

        val expected = "@Test void (){\n" +
            "        // contents\n" +
            "    }"

        TestCase.assertEquals(expected, template.templateText)
    }

    @Test
    fun testModifiers() {

        this.myFixture.configureByFile("/Tests.java")
        val serv = TemplateFactoryService(project)

        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        val methodToBeDuplicated = testClass.findMethodsByName("hasModifiers")[0] as PsiMethod
        val template = serv.createTemplate(methodToBeDuplicated)

        val expected = "@Test\n" +
            "    public static void (){\n" +
            "        // contents\n" +
            "    }"

        TestCase.assertEquals(expected, template.templateText)
    }

    @Test
    fun testReturnType() {

        this.myFixture.configureByFile("/Tests.java")
        val serv = TemplateFactoryService(project)

        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        val methodToBeDuplicated = testClass.findMethodsByName("hasReturnTy")[0] as PsiMethod
        val template = serv.createTemplate(methodToBeDuplicated)

        val expected = "@Test String (){\n" +
            "        // contents\n" +
            "    }"

        TestCase.assertEquals(expected, template.templateText)
    }

    @Test
    fun testParams() {

        this.myFixture.configureByFile("/Tests.java")
        val serv = TemplateFactoryService(project)

        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        val methodToBeDuplicated = testClass.findMethodsByName("hasParams")[0] as PsiMethod
        val template = serv.createTemplate(methodToBeDuplicated)

        val expected = "@Test void (int x, int y){\n" +
            "        // contents\n" +
            "    }"

        TestCase.assertEquals(expected, template.templateText)
    }

    @Test
    fun testTypeParams() {

        this.myFixture.configureByFile("/Tests.java")
        val serv = TemplateFactoryService(project)

        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        val methodToBeDuplicated = testClass.findMethodsByName("hasTypeParams")[0] as PsiMethod
        val template = serv.createTemplate(methodToBeDuplicated)

        val expected = "@Test <A, B>void (){\n" +
            "        // contents\n" +
            "    }"

        TestCase.assertEquals(expected, template.templateText)
    }

    @Test
    fun testThrows() {

        this.myFixture.configureByFile("/Tests.java")
        val serv = TemplateFactoryService(project)

        val testClass = PsiTreeUtil.findChildOfType(myFixture.file, PsiClass::class.java)!!
        val methodToBeDuplicated = testClass.findMethodsByName("throwsException")[0] as PsiMethod
        val template = serv.createTemplate(methodToBeDuplicated)

        val expected = "@Test void ()throws Exception{\n" +
            "        // contents\n" +
            "    }"

        TestCase.assertEquals(expected, template.templateText)
    }
}
