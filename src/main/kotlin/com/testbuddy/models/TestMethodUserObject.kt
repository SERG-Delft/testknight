package com.testbuddy.models

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project

data class TestMethodUserObject(val reference: TestMethodData, val project: Project?, val editor: Editor?)
