package com.github.jorgeromeu.testbuddy.services

import com.github.jorgeromeu.testbuddy.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
