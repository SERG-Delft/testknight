package com.testbuddy.services

import com.intellij.openapi.project.Project
import com.testbuddy.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
