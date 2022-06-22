package com.github.marioplus.xmetasupport.action

import com.github.marioplus.xmetasupport.Constant
import com.intellij.icons.AllIcons
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.ModuleTypeId
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessModuleDir
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.ModuleRootModel
import com.intellij.openapi.util.Comparing
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.util.io.exists
import com.intellij.util.io.isFile
import org.jetbrains.jps.model.java.JavaResourceRootType
import org.jetbrains.jps.model.java.JavaSourceRootProperties
import org.jetbrains.jps.model.java.JavaSourceRootType
import org.jetbrains.jps.model.java.JpsJavaExtensionService
import java.nio.file.Paths
import java.util.*
import javax.swing.Icon

/**
 * 标记xmeta_gen目录为
 *
 * @author marioplus
 * @since 1.0.0
 */
class MarkAsAction(private val markAsType: FolderType) : AnAction(markAsType.typeName) {
    companion object {
        private val LOG = logger<MarkAsAction>()
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
        GENERATED_SOURCE("源码文件夹", AllIcons.Modules.GeneratedSourceRoot) {
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

    override fun actionPerformed(event: AnActionEvent) {
        val project: Project = event.project ?: return;
        ModuleManager.getInstance(project).modules
            .forEach {
                if (isJavaModule(it)) {
                    markFolderAs(project, it, Constant.X_META_GEN)
                } else {
                    LOG.debug("模块（${it.name}）不是 Java 模块")
                }
            }
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = Objects.nonNull(event.project)
    }

    /**
     * 判断模块是否为java模块
     */
    private fun isJavaModule(module: Module): Boolean {
        val moduleType = ModuleType.get(module)
        return moduleType.id == ModuleTypeId.JAVA_MODULE
    }

    /**
     * 提交模块状态
     */
    private fun commitModel(module: Module, model: ModifiableRootModel) {
        ApplicationManager.getApplication().runWriteAction {
            model.commit()
            module.project.save()
        }
    }

    /**
     * 寻找内容
     */
    private fun findContentEntry(model: ModuleRootModel, vFile: VirtualFile): ContentEntry? {
        return model.contentEntries
            .find { it.file != null && VfsUtilCore.isAncestor(it.file!!, vFile, false) }
    }

    /**
     * 批量修改根
     */
    private fun modifyRoots(module: Module, vFile: VirtualFile) {

        val modifiableModel = ModuleRootManager.getInstance(module).modifiableModel
        val entry = findContentEntry(modifiableModel, vFile) ?: return
        val sourceFolders = entry.sourceFolders
        for (sourceFolder in sourceFolders) {
            if (Comparing.equal(sourceFolder.file, vFile)) {
                entry.removeSourceFolder(sourceFolder)
                break
            }
        }
        markAsType.markAs(entry, vFile)
        Notifications.Bus.notify(
            Notification(
                Notifications.SYSTEM_MESSAGES_GROUP_ID,
                "XmetaSupport",
                "需要将 ${vFile.path} 标记为 ${markAsType.typeName}",
                NotificationType.INFORMATION
            )
        )
        commitModel(module, modifiableModel)
    }

    /**
     * 添加源码目录
     * @param project 当前项目
     * @param module 模块
     * @param folderPath ，需要标记的文件夹，模块下的相对路径
     */
    private fun markFolderAs(project: Project, module: Module, folderPath: String) {
        val path = Paths.get(getRootVf(module).path + folderPath)
        if (!path.exists() || path.isFile()) {
            LOG.debug("模块（${module.name}）不含 xmeta_gen 目录")
            return
        }

        val vf = VirtualFileManager.getInstance().findFileByNioPath(path) ?: return
        modifyRoots(module, vf)
    }

    private fun getRootVf(module: Module): VirtualFile {
        return ModuleRootManager.getInstance(module)
            .modifiableModel
            .contentRoots[0]
    }

}