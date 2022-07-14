package com.github.marioplus.xmetasupport.action

import com.github.marioplus.xmetasupport.ext.module.findVfByPath
import com.github.marioplus.xmetasupport.ext.module.isJavaModule
import com.github.marioplus.xmetasupport.ext.project.getModules
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import java.util.*

/**
 * 清理xmeta生成的文件夹
 *
 * @author marioplus
 * @since 1.0.1
 */
abstract class BaseCleanBuildFolderAction : AnAction() {

    companion object {
        private val LOG = logger<BaseCleanBuildFolderAction>()
    }

    /**
     * 获取build产生的文件夹路径
     */
    abstract fun getBuildFolderPaths(): List<String>

    override fun actionPerformed(event: AnActionEvent) {
        val project: Project = event.project ?: return
        project.getModules()
            .filter { it.isJavaModule() }
            .forEach { deleteVfByPath(it, this.getBuildFolderPaths()) }
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = Objects.nonNull(event.project)
    }

    private fun deleteVfByPath(module: Module, folderPaths: List<String>) {
        folderPaths.forEach { folderPath ->
            val vf = module.findVfByPath(folderPath) ?: return
            ApplicationManager.getApplication().runWriteAction {
                vf.delete(this)
                Notifications.Bus.notify(
                    Notification(
                        Notifications.SYSTEM_MESSAGES_GROUP_ID,
                        "XmetaSupport",
                        "已删除 ${module.name} 的 ${vf.name} 文件夹",
                        NotificationType.INFORMATION
                    )
                )
            }
        }
    }
}