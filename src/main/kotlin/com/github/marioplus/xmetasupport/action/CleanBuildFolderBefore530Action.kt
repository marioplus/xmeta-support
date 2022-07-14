package com.github.marioplus.xmetasupport.action

import com.github.marioplus.xmetasupport.Constant

/**
 * 清理xmeta生成的文件夹
 * 5.3.0之前
 * @author marioplus
 * @since 1.0.1
 */
class CleanBuildFolderBefore530Action : BaseCleanBuildFolderAction() {

    override fun getBuildFolderPaths(): List<String> {
        return listOf(Constant.X_META_GEN, Constant.X_META_TEMP)
    }
}