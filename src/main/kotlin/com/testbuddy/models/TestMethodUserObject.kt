package com.testbuddy.models

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project

/**
 * Custom user object which gets passed into testListTree which contains information used by the tree nodes.
 *
 * @param reference TestMethodData of the test method.
 * @param project The current project if open.
 * @param editor The current editor if open.
 */
data class TestMethodUserObject(val reference: TestMethodData, val project: Project?, val editor: Editor?)
