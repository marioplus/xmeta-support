package com.github.marioplus.xmetasupport.action

import com.github.marioplus.xmetasupport.ext.module.findVfByPath
import com.github.marioplus.xmetasupport.ext.module.isJavaModule
import com.github.marioplus.xmetasupport.ext.module.modifyVfEntryAutoCommit
import com.intellij.icons.AllIcons
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.util.Comparing
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.jps.model.java.JavaResourceRootType
import org.jetbrains.jps.model.java.JavaSourceRootProperties
import org.jetbrains.jps.model.java.JavaSourceRootType
import org.jetbrains.jps.model.java.JpsJavaExtensionService
import java.util.*
import javax.swing.Icon

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

    enum class FolderType(val typeName: String, icon: Icon?) {

        NORMAL("普通文件夹", null) {
            override fun markAs(entry: ContentEntry, vFile: VirtualFile) {
                return
            }
        },
        RESOURCE("资源文件夹", AllIcons.Modules.ResourcesRoot) {
            override fun markAs(entry: ContentEntry, vFile: VirtualFile) {
                entry.addSourceFolder(vFile, JavaResourceRootType.RESOURCE)
            }
        },
        TEST_RESOURCE("测试资源文件夹", AllIcons.Modules.TestResourcesRoot) {
            override fun markAs(entry: ContentEntry, vFile: VirtualFile) {
                entry.addSourceFolder(vFile, JavaResourceRootType.TEST_RESOURCE)
            }
        },
        SOURCE("源码文件夹", AllIcons.Modules.SourceRoot) {
            override fun markAs(entry: ContentEntry, vFile: VirtualFile) {
                entry.addSourceFolder(vFile, JavaSourceRootType.SOURCE)
            }
        },
        TEST_SOURCE("测试源码文件夹", AllIcons.Modules.TestRoot) {
            override fun markAs(entry: ContentEntry, vFile: VirtualFile) {
                entry.addSourceFolder(vFile, JavaSourceRootType.TEST_SOURCE)
            }
        },
        GENERATED_SOURCE("生成源码文件夹", AllIcons.Modules.GeneratedSourceRoot) {
            override fun markAs(entry: ContentEntry, vFile: VirtualFile) {
                val sourceRootProperties: JavaSourceRootProperties = JpsJavaExtensionService.getInstance()
                    .createSourceRootProperties("", true)
                entry.addSourceFolder(vFile, JavaSourceRootType.SOURCE, sourceRootProperties)
            }
        },
        ;

        /**
         * 标记为此类型的方法
         * @param entry 内容属性
         * @param vFile 虚拟文件目录
         */
        abstract fun markAs(entry: ContentEntry, vFile: VirtualFile)
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
