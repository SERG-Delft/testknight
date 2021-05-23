package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.testbuddy.views.trees.ChecklistCellRenderer
import org.junit.Before
import org.junit.Test

class ChecklistTreeServiceTest : BasePlatformTestCase() {

    private val service = ChecklistTreeService()
    private val checklistService = GenerateTestCaseChecklistService()

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testInit() {

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        val expected = ""
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testReset() {

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val testMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklist = checklistService.generateClassChecklistFromClass(psiClass)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklist)
        service.addChecklist(backendChecklist)
        service.resetTree()
        val expected = ""
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testAddChecklistMethodOnce() {

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklist = checklistService.generateClassChecklistFromMethod(psiMethod)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklist)
        val expected = "Math2 add Test where a is empty\n" +
            "Math2 add Test where a has one element\n" +
            "Math2 add Test where a is null\n" +
            "Math2 add Test where foreach loop runs multiple times\n"
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testAddChecklistMethodTwice() {

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklist = checklistService.generateClassChecklistFromMethod(psiMethod)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklist)
        service.addChecklist(backendChecklist)
        val expected = "Math2 add Test where a is empty\n" +
            "Math2 add Test where a has one element\n" +
            "Math2 add Test where a is null\n" +
            "Math2 add Test where foreach loop runs multiple times\n"
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testAddChecklistClassOnce() {

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklist = checklistService.generateClassChecklistFromClass(psiClass)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklist)
        val expected = "Math2 add Test where a is empty\n" +
            "Math2 add Test where a has one element\n" +
            "Math2 add Test where a is null\n" +
            "Math2 add Test where foreach loop runs multiple times\n" +
            "Math2 add2 Test where a is empty\n" +
            "Math2 add2 Test where a has one element\n" +
            "Math2 add2 Test where a is null\n" +
            "Math2 add2 Test where foreach loop runs multiple times\n"
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testAddChecklistClassTwice() {

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklist = checklistService.generateClassChecklistFromClass(psiClass)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklist)
        service.addChecklist(backendChecklist)
        val expected = "Math2 add Test where a is empty\n" +
            "Math2 add Test where a has one element\n" +
            "Math2 add Test where a is null\n" +
            "Math2 add Test where foreach loop runs multiple times\n" +
            "Math2 add2 Test where a is empty\n" +
            "Math2 add2 Test where a has one element\n" +
            "Math2 add2 Test where a is null\n" +
            "Math2 add2 Test where foreach loop runs multiple times\n"
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testAddChecklistMethodThenClass() {

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)
        val backendChecklistMethod = checklistService.generateClassChecklistFromMethod(psiMethod)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklistMethod)
        service.addChecklist(backendChecklistClass)
        val expected = "Math2 add Test where a is empty\n" +
            "Math2 add Test where a has one element\n" +
            "Math2 add Test where a is null\n" +
            "Math2 add Test where foreach loop runs multiple times\n" +
            "Math2 add2 Test where a is empty\n" +
            "Math2 add2 Test where a has one element\n" +
            "Math2 add2 Test where a is null\n" +
            "Math2 add2 Test where foreach loop runs multiple times\n"
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testAddChecklistClassThenMethod() {

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)
        val backendChecklistMethod = checklistService.generateClassChecklistFromMethod(psiMethod)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklistClass)
        service.addChecklist(backendChecklistMethod)
        val expected = "Math2 add Test where a is empty\n" +
            "Math2 add Test where a has one element\n" +
            "Math2 add Test where a is null\n" +
            "Math2 add Test where foreach loop runs multiple times\n" +
            "Math2 add2 Test where a is empty\n" +
            "Math2 add2 Test where a has one element\n" +
            "Math2 add2 Test where a is null\n" +
            "Math2 add2 Test where foreach loop runs multiple times\n"
        val received = service.print()
        assertEquals(expected, received)
    }
}
