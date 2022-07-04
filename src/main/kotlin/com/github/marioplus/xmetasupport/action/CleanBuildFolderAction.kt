package com.github.marioplus.xmetasupport.action

import com.github.marioplus.xmetasupport.Constant
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
class CleanBuildFolderAction : AnAction() {

    private val folderPaths = listOf(Constant.X_META_GEN, Constant.X_META_TEMP)

    companion object {
        private val LOG = logger<CleanBuildFolderAction>()
    }

    override fun actionPerformed(event: AnActionEvent) {
        val project: Project = event.project ?: return
        project.getModules()
            .filter { it.isJavaModule() }
            .forEach { deleteVf(it, folderPaths) }
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = Objects.nonNull(event.project)
    }

    private fun deleteVf(module: Module, folderPaths: List<String>) {
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