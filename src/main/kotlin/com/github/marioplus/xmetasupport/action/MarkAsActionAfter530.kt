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
 * 5.3.0之后
 *
 * @author marioplus
 * @since 1.0.0
 */
class MarkAsActionAfter530 : BaseMarkAsAction() {

    override fun getMarkAsFoldersDefinition(): Map<String, FolderType> {
        return mapOf(
            Constant.X_META_GEN to FolderType.SOURCE,
            Constant.X_META_TEMP to FolderType.GENERATED_SOURCE,
        )
    }
}