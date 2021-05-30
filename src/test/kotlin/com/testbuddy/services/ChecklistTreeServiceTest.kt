package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.testbuddy.com.testbuddy.extensions.TestBuddyTestCase
import com.testbuddy.models.testingChecklist.leafNodes.CustomChecklistNode
import com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import com.testbuddy.views.trees.ChecklistCellRenderer
import org.junit.Test

class ChecklistTreeServiceTest : TestBuddyTestCase() {

    private val service = ChecklistTreeService()
    private val checklistService = GenerateTestCaseChecklistService()

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

    @Test
    fun testDeleteClassExistClass() {

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
        service.deleteClass(backendChecklistClass)
        val expected = ""
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testDeleteClassExistMethod() {

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
        service.deleteClass(backendChecklistMethod)
        val expected = ""
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testDeleteClassNotExists() {

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
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

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
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

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
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

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
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

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
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

        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
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
        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)

        val deleteItem = CustomChecklistNode(backendChecklistClass.children[0].children[0].description, backendChecklistClass.children[0].children[0].element, 0)
        val deleteMethod = TestingChecklistMethodNode(backendChecklistClass.children[0].description, backendChecklistClass.children[0].children, backendChecklistClass.children[0].element, 0)
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
        this.myFixture.configureByFile("/Math2.java")
        val psi = this.myFixture.file
        val project = this.myFixture.project
        val psiClass = PsiTreeUtil.findChildOfType(psi, PsiClass::class.java)
        val psiMethod = psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(psiClass)

        val deleteItem = CustomChecklistNode(backendChecklistClass.children[0].children[0].description, backendChecklistClass.children[0].children[0].element, 0)
        val deleteMethod = TestingChecklistMethodNode(backendChecklistClass.children[0].description, backendChecklistClass.children[0].children, backendChecklistClass.children[0].element, 0)
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
        service.deleteClass(backendChecklistClass)
        service.deleteClass(backendChecklistClass)

        val expected = ""
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testDeleteMethodTwice() {

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
        service.deleteMethod(backendChecklistClass.children[0], backendChecklistClass)
        service.deleteMethod(backendChecklistClass.children[0], backendChecklistClass)

        val expected = ""
        val received = service.print()
        assertEquals(expected, received)
    }
}
