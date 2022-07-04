package com.github.marioplus.xmetasupport.ext.project

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project

/**
 * project 拓展
 *
 * @author marioplus
 * @since 1.0.1
 */

/**
 * 获取模块列表
 */
fun Project.getModules(): Array<out Module> {
    return ModuleManager.getInstance(this).modules
}