package com.github.marioplus.xmetasupport.action

import com.github.marioplus.xmetasupport.Constant

/**
 * 清理xmeta生成的文件夹
 * 5.3.0之后
 * @author marioplus
 * @since 1.0.1
 */
class CleanBuildFolderAfter530Action : BaseCleanBuildFolderAction() {

    override fun getBuildFolderPaths(): List<String> {
        return listOf(Constant.X_META_YAML, Constant.X_META_TEMP)
    }
}