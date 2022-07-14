package com.github.marioplus.xmetasupport.enums

import com.intellij.icons.AllIcons
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.jps.model.java.JavaResourceRootType
import org.jetbrains.jps.model.java.JavaSourceRootProperties
import org.jetbrains.jps.model.java.JavaSourceRootType
import org.jetbrains.jps.model.java.JpsJavaExtensionService
import javax.swing.Icon

/**
 * 文件类型
 *
 * @author marioplus
 * @since 1.2.0
 */
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