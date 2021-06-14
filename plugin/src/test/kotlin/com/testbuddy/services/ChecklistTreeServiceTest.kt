package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.models.testingChecklist.leafNodes.CustomChecklistNode
import com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import com.testbuddy.views.trees.ChecklistCellRenderer
import org.junit.Test

class ChecklistTreeServiceTest : TestBuddyTestCase() {

    @Test
    fun testInit() {

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)

        // random file to get project instance
        this.myFixture.configureByFile("/Math2.java")
        val service = ChecklistTreeService(this.myFixture.project)
        service.initTrees(checkListTree)
        val expected = ""
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testReset() {
        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
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
        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
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
        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
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
        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
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
        println("RECEOED:\n $received")
        assertEquals(expected, received)
    }

    @Test
    fun testAddChecklistClassTwice() {
        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
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

        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
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

        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
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

    @Test
    fun testDeleteClassExistClass() {

        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)
        val backendChecklistMethod = checklistService.generateClassChecklistFromMethod(psiMethod)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklistClass)
        service.addChecklist(backendChecklistMethod)
        service.deleteClass(backendChecklistClass)
        val expected = ""
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testDeleteClassExistMethod() {

        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)
        val backendChecklistMethod = checklistService.generateClassChecklistFromMethod(psiMethod)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklistClass)
        service.addChecklist(backendChecklistMethod)
        service.deleteClass(backendChecklistMethod)
        val expected = ""
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testDeleteClassNotExists() {

        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.deleteClass(backendChecklistClass)
        val expected = ""
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testDeleteMethodExist() {

        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklistClass)
        service.deleteMethod(backendChecklistClass.children[0], backendChecklistClass)

        val expected = "Math2 add2 Test where a is empty\n" +
            "Math2 add2 Test where a has one element\n" +
            "Math2 add2 Test where a is null\n" +
            "Math2 add2 Test where foreach loop runs multiple times\n"
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testDeleteMethodNonExistClass() {
        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.deleteMethod(backendChecklistClass.children[0], backendChecklistClass)

        val expected = ""
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testDeleteMethodNonExistMethod() {
        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)
        val backendChecklistMethod = checklistService.generateClassChecklistFromMethod(psiMethod)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklistMethod)
        service.deleteMethod(backendChecklistClass.children[1], backendChecklistClass)

        val expected = "Math2 add Test where a is empty\n" +
            "Math2 add Test where a has one element\n" +
            "Math2 add Test where a is null\n" +
            "Math2 add Test where foreach loop runs multiple times\n"
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testDeleteItemExists() {
        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklistClass)
        service.deleteItem(backendChecklistClass.children[0].children[0], backendChecklistClass.children[0], backendChecklistClass)

        val expected = "Math2 add Test where a has one element\n" +
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
    fun testDeleteItemNonExists() {
        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklistClass)
        val deleteItem = CustomChecklistNode(backendChecklistClass.children[0].children[0].description, backendChecklistClass.children[0].children[0].element, 0)
        service.deleteItem(deleteItem, backendChecklistClass.children[0], backendChecklistClass)
        service.deleteItem(deleteItem, backendChecklistClass.children[0], backendChecklistClass)

        val expected = "Math2 add Test where a has one element\n" +
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
    fun testDeleteItemTwice() {
        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklistClass)
        service.deleteItem(backendChecklistClass.children[0].children[0], backendChecklistClass.children[0], backendChecklistClass)
        service.deleteItem(backendChecklistClass.children[0].children[0], backendChecklistClass.children[0], backendChecklistClass)

        val expected = "Math2 add Test where a is null\n" +
            "Math2 add Test where foreach loop runs multiple times\n" +
            "Math2 add2 Test where a is empty\n" +
            "Math2 add2 Test where a has one element\n" +
            "Math2 add2 Test where a is null\n" +
            "Math2 add2 Test where foreach loop runs multiple times\n"
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testDeleteItemNonExistsMethod() {
        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)

        val deleteItem = CustomChecklistNode(backendChecklistClass.children[0].children[0].description, backendChecklistClass.children[0].children[0].element)
        val deleteMethod = TestingChecklistMethodNode(backendChecklistClass.children[0].description, backendChecklistClass.children[0].children, backendChecklistClass.children[0].element)
        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklistClass)
        service.deleteMethod(deleteMethod, backendChecklistClass)
        service.deleteItem(deleteItem, deleteMethod, backendChecklistClass)

        val expected = "Math2 add2 Test where a is empty\n" +
            "Math2 add2 Test where a has one element\n" +
            "Math2 add2 Test where a is null\n" +
            "Math2 add2 Test where foreach loop runs multiple times\n"
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testDeleteItemNonExistClass() {
        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)

        val deleteItem = CustomChecklistNode(backendChecklistClass.children[0].children[0].description, backendChecklistClass.children[0].children[0].element)
        val deleteMethod = TestingChecklistMethodNode(backendChecklistClass.children[0].description, backendChecklistClass.children[0].children, backendChecklistClass.children[0].element)
        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklistClass)
        service.deleteClass(backendChecklistClass)
        service.deleteItem(deleteItem, deleteMethod, backendChecklistClass)
        val expected = ""
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testDeleteClassTwice() {
        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)
        val backendChecklistMethod = checklistService.generateClassChecklistFromMethod(psiMethod)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklistClass)
        service.addChecklist(backendChecklistMethod)
        service.deleteClass(backendChecklistClass)
        service.deleteClass(backendChecklistClass)

        val expected = ""
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testDeleteMethodTwice() {
        val checklistService = GenerateTestCaseChecklistService()

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val service = ChecklistTreeService(project)
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)
        val backendChecklistMethod = checklistService.generateClassChecklistFromMethod(psiMethod)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklistMethod)
        service.deleteMethod(backendChecklistClass.children[0], backendChecklistClass)
        service.deleteMethod(backendChecklistClass.children[0], backendChecklistClass)

        val expected = ""
        val received = service.print()
        assertEquals(expected, received)
    }
}
