package com.testbuddy.com.testbuddy.models

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project

/**
 * The user object which is stored inside the node of the test case tree.
 * Contains additional information such as the current project and current editor (which can be null)
 *
 * @param reference reference to the TestMethodData
 * @param project Current project, can be null.
 * @param editor Current editor, can be null.
 */
data class TestMethodUserObject(val reference: TestMethodData, val project: Project?, val editor: Editor?)
