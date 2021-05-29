package com.testbuddy.models

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.util.xmlb.Converter
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class TestingChecklist(val classChecklists: MutableList<TestingChecklistClassNode>) :
    Converter<TestingChecklist>() {
    override fun toString(value: TestingChecklist): String? {

        println("sunt la to string")
        println(Json.encodeToString(value))
        return Json.encodeToString(value)
    }

    override fun fromString(value: String): TestingChecklist {
        println("sunt la from string")
        println(Json.decodeFromString<TestingChecklist>(value))
        return Json.decodeFromString<TestingChecklist>(value)
    }
}

@Serializable
open class TestingChecklistNode(open var checked: Int = 0)
@Serializable
data class TestingChecklistLeafNode(
    var description: String,
    val element: PsiElement,
    // val parent: TestingChecklistMethodNode
) : TestingChecklistNode()
//    override fun toString(value: TestingChecklistLeafNode): String? {
//        return Json.encodeToString(value)
//    }
//
//    override fun fromString(value: String): TestingChecklistLeafNode?
//    { return Json.decodeFromString<TestingChecklistLeafNode>(value)
//    }
// }

abstract class TestingChecklistParentNode : TestingChecklistNode()
@Serializable
data class TestingChecklistClassNode(
    var description: String,
    val children: MutableList<TestingChecklistMethodNode>,
    val element: PsiClass,
    // override var checked: Int = 0
    // val parent: TestingChecklist
) : TestingChecklistParentNode()
@Serializable
data class TestingChecklistMethodNode(
    var description: String,
    val children: MutableList<TestingChecklistLeafNode>,
    val element: PsiMethod,
    // override var checked: Int = 0
    // val parent: TestingChecklistClassNode?
) : TestingChecklistParentNode()
