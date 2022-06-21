package com.github.marioplus.xmetasupport.services

import com.intellij.openapi.project.Project
import com.github.marioplus.xmetasupport.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
