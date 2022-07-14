package com.github.marioplus.xmetasupport.action

import com.github.marioplus.xmetasupport.enums.FolderType
import com.github.marioplus.xmetasupport.ext.module.findVfByPath
import com.github.marioplus.xmetasupport.ext.module.isJavaModule
import com.github.marioplus.xmetasupport.ext.module.modifyVfEntryAutoCommit
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Comparing
import java.util.*

/**
 * 基础文件夹类型标记类
 *
 * @author marioplus
 * @since 1.0.0
 */
abstract class BaseMarkAsAction : AnAction() {

    companion object {
        private val LOG = logger<BaseMarkAsAction>()
    }

    abstract fun getMarkAsFoldersDefinition(): Map<String, FolderType>

    override fun actionPerformed(event: AnActionEvent) {
        val project: Project = event.project ?: return
        getMarkAsFoldersDefinition().forEach { folderEntry ->
            val path: String = folderEntry.key
            val type: FolderType = folderEntry.value
            ModuleManager.getInstance(project).modules
                .forEach { module ->
                    if (module.isJavaModule()) {
                        markFolderAs(module, path, type)
                    } else {
                        LOG.debug("模块（${module.name}）不是 Java 模块")
                    }
                }
        }
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = Objects.nonNull(event.project)
    }

    /**
     * 添加源码目录
     * @param module 模块
     * @param folderPath ，需要标记的文件夹，模块下的相对路径
     * @param folderType
     */
    private fun markFolderAs(module: Module, folderPath: String, folderType: FolderType) {
        val vf = module.findVfByPath(folderPath) ?: return
        module.modifyVfEntryAutoCommit(vf) { entry ->
            val sourceFolders = entry.sourceFolders
            for (sourceFolder in sourceFolders) {
                if (Comparing.equal(sourceFolder.file, vf)) {
                    entry.removeSourceFolder(sourceFolder)
                    break
                }
            }
            folderType.markAs(entry, vf)
            Notifications.Bus.notify(
                Notification(
                    Notifications.SYSTEM_MESSAGES_GROUP_ID,
                    "XmetaSupport",
                    "已将 ${module.name} 的 ${vf.name} 标记为 ${folderType.typeName}",
                    NotificationType.INFORMATION
                )
            )
        }
    }


}
