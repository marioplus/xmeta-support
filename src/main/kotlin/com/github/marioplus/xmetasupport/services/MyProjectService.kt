package com.github.marioplus.xmetasupport.services

import com.github.marioplus.xmetasupport.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
