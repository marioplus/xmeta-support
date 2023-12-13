package com.github.marioplus.xmetasupport.ext.module

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.ModuleTypeId
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import java.nio.file.Paths
import java.util.function.Consumer
import kotlin.io.path.isDirectory

/**
 * module 相关拓展
 *
 * @author marioplus
 * @since 1.0.1
 */


/**
 * 判断模块是否为java模块
 */
fun Module.isJavaModule(): Boolean {
    val moduleType = ModuleType.get(this)
    return moduleType.id == ModuleTypeId.JAVA_MODULE
}

/**
 * 获取根 vf
 */
fun Module.getRootVf(): VirtualFile {
    return ModuleRootManager.getInstance(this)
        .modifiableModel
        .contentRoots[0]
}

/**
 * 根据路径查找vf
 */
fun Module.findVfByPath(folderPath: String): VirtualFile? {
    val path = Paths.get(this.getRootVf().path + folderPath)
    if (!path.isDirectory()) {
        this.thisLogger().debug("模块（${this.name}）不含 $folderPath 目录")
        return null
    }

    return VirtualFileManager.getInstance().findFileByNioPath(path)
}

/**
 * 修改文件状态
 */
fun Module.modifyVfEntryAutoCommit(vf: VirtualFile, modifyConsumer: Consumer<ContentEntry>) {
    val modifiableModel = ModuleRootManager.getInstance(this).modifiableModel
    val entry = this.findContentEntry(modifiableModel, vf) ?: return
    modifyConsumer.accept(entry)
    // 提交模块状态
    ApplicationManager.getApplication().runWriteAction(modifiableModel::commit)
    this.project.save()
}

/**
 * 寻找内容
 */
fun Module.findContentEntry(modifiableModel: ModifiableRootModel, vf: VirtualFile): ContentEntry? {
    return modifiableModel.contentEntries
        .find { it.file != null && VfsUtilCore.isAncestor(it.file!!, vf, false) }
}


