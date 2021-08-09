package com.testknight.services

import com.intellij.psi.PsiMethod
import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.testknight.extensions.TestKnightTestCase
import com.testknight.models.ChecklistUserObject
import com.testknight.models.testingChecklist.leafNodes.CustomChecklistNode
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistClassNode
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import com.testknight.views.trees.ChecklistCellRenderer
import io.mockk.spyk
import io.mockk.verify
import org.junit.Test

class ChecklistTreeServiceTest : TestKnightTestCase() {

    @Test
    fun testInit() {
        val data = getBasicTestInfo("/Math2.java")

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)

        // random file to get project instance
        val service = ChecklistTreeService(data.project)
        service.initTrees(checkListTree)
        val expected = ""
        val received = service.print()

        assertEquals(expected, received)
    }

    @Test
    fun testReset() {
        val data = getBasicTestInfo("/Math2.java")

        val service = ChecklistTreeService(project)
        val checklistService = GenerateTestCaseChecklistService()
        val backendChecklist = checklistService.generateClassChecklistFromClass(data.psiClass!!)
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
        val data = getBasicTestInfo("/Math2.java")

        val service = ChecklistTreeService(project)
        val checklistService = GenerateTestCaseChecklistService()
        val psiMethod = data.psiClass!!.findMethodsByName("add")[0] as PsiMethod
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
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val psiMethod = data.psiClass!!.findMethodsByName("add")[0] as PsiMethod
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
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val backendChecklist = checklistService.generateClassChecklistFromClass(data.psiClass!!)
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
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val backendChecklist = checklistService.generateClassChecklistFromClass(data.psiClass!!)
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
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val psiMethod = data.psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(data.psiClass!!)
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
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val psiMethod = data.psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(data.psiClass!!)
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
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val psiMethod = data.psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(data.psiClass!!)
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
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val psiMethod = data.psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(data.psiClass!!)
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
    fun testDeleteMethodExist() {
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(data.psiClass!!)

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
    fun testDeleteClassNotExists() {
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(data.psiClass!!)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.deleteClass(backendChecklistClass)
        val expected = ""
        val received = service.print()
        assertEquals(expected, received)
    }

    @Test
    fun testDeleteMethodNonExistMethod() {
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val psiMethod = data.psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(data.psiClass!!)
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
    fun testDeleteMethodNonExistClass() {
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(data.psiClass!!)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.deleteMethod(backendChecklistClass.children[0], backendChecklistClass)

        val expected = ""
        val received = service.print()

        assertEquals(expected, received)
    }

    @Test
    fun testDeleteItemExists() {
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(data.psiClass!!)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklistClass)
        service.deleteItem(
            backendChecklistClass.children[0].children[0],
            backendChecklistClass.children[0],
            backendChecklistClass
        )

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
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(data.psiClass!!)
        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklistClass)
        service.deleteItem(
            backendChecklistClass.children[0].children[0],
            backendChecklistClass.children[0],
            backendChecklistClass
        )
        service.deleteItem(
            backendChecklistClass.children[0].children[0],
            backendChecklistClass.children[0],
            backendChecklistClass
        )

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
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(data.psiClass!!)

        val deleteItem = CustomChecklistNode(
            backendChecklistClass.children[0].children[0].description,
            backendChecklistClass.children[0].children[0].element
        )
        val deleteMethod = TestingChecklistMethodNode(
            backendChecklistClass.children[0].description,
            backendChecklistClass.children[0].children,
            backendChecklistClass.children[0].element
        )
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
    fun testDeleteItemNonExists() {
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(data.psiClass!!)

        val root = CheckedTreeNode("root")
        val checkListTree = CheckboxTree(ChecklistCellRenderer(true), root)
        service.initTrees(checkListTree)
        service.addChecklist(backendChecklistClass)
        val deleteItem = CustomChecklistNode(
            backendChecklistClass.children[0].children[0].description,
            backendChecklistClass.children[0].children[0].element,
            0
        )
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
    fun testDeleteClassTwice() {
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val psiMethod = data.psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(data.psiClass!!)
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
    fun testDeleteItemNonExistClass() {
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(data.psiClass!!)

        val deleteItem = CustomChecklistNode(
            backendChecklistClass.children[0].children[0].description,
            backendChecklistClass.children[0].children[0].element
        )
        val deleteMethod = TestingChecklistMethodNode(
            backendChecklistClass.children[0].description,
            backendChecklistClass.children[0].children,
            backendChecklistClass.children[0].element
        )
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
    fun testDeleteMethodTwice() {
        val data = getBasicTestInfo("/Math2.java")

        val checklistService = GenerateTestCaseChecklistService()
        val service = ChecklistTreeService(project)
        val psiMethod = data.psiClass!!.findMethodsByName("add")[0] as PsiMethod
        val backendChecklistClass = checklistService.generateClassChecklistFromClass(data.psiClass!!)
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

    @Test
    fun testDeleteElementChecklistClassNode() {
        val data = getBasicTestInfo("/Math2.java")

        val serv = spyk(ChecklistTreeService(data.project))
        serv.initUiTree()
        val node = CheckedTreeNode()
        node.setParent(CheckedTreeNode())

        val testingChecklistClassNode = TestingChecklistClassNode()
        (node.parent as CheckedTreeNode).userObject = ChecklistUserObject(testingChecklistClassNode)
        ((node.parent as CheckedTreeNode).userObject as ChecklistUserObject).checklistNode = testingChecklistClassNode
        val checklistUserObject = ChecklistUserObject(testingChecklistClassNode)
        checklistUserObject.checklistNode = testingChecklistClassNode
        node.userObject = checklistUserObject

        serv.deleteElement(node)

        verify { serv.deleteClass(any()) }
    }

    @Test
    fun testDeleteElementChecklistMethodNode() {
        val data = getBasicTestInfo("/Math2.java")

        val serv = spyk(ChecklistTreeService(data.project))
        serv.initUiTree()

        val parent = CheckedTreeNode()
        val node = CheckedTreeNode()
        parent.insert(node, 0)

        val testingChecklistMethodNode = TestingChecklistMethodNode()
        val testingChecklistClassNode = TestingChecklistClassNode()
        node.userObject = ChecklistUserObject(testingChecklistMethodNode)
        (node.userObject as ChecklistUserObject).checklistNode = testingChecklistMethodNode
        parent.userObject = ChecklistUserObject(testingChecklistClassNode)
        (parent.userObject as ChecklistUserObject).checklistNode = testingChecklistClassNode

        serv.deleteElement(node)

        verify { serv.deleteMethod(any(), any()) }
    }

    @Test
    fun testDeleteElementChecklistItemNode() {
        val data = getBasicTestInfo("/Math2.java")

        val serv = spyk(ChecklistTreeService(data.project))
        serv.initUiTree()

        val grandparent = CheckedTreeNode()
        val parent = CheckedTreeNode()
        val node = CheckedTreeNode()
        grandparent.insert(parent, 0)
        parent.insert(node, 0)
        val testingChecklistNode = CustomChecklistNode()
        val testingChecklistMethodNode = TestingChecklistMethodNode()
        val testingChecklistClassNode = TestingChecklistClassNode()
        node.userObject = ChecklistUserObject(testingChecklistNode)
        (node.userObject as ChecklistUserObject).checklistNode = testingChecklistNode
        parent.userObject = ChecklistUserObject(testingChecklistMethodNode)
        (parent.userObject as ChecklistUserObject).checklistNode = testingChecklistMethodNode
        grandparent.userObject = ChecklistUserObject(testingChecklistClassNode)
        (grandparent.userObject as ChecklistUserObject).checklistNode = testingChecklistClassNode

        serv.deleteElement(node)

        verify { serv.deleteItem(any(), any(), any()) }
    }
}
