package com.github.marioplus.xmetasupport.action

import com.github.marioplus.xmetasupport.Constant
import com.github.marioplus.xmetasupport.enums.FolderType

/**
 * 取消标记xmeta文件夹类型
 *
 * @author marioplus
 * @since 1.2.0
 */
class UnMarkedAction : BaseMarkAsAction() {
    override fun getMarkAsFoldersDefinition(): Map<String, FolderType> {
        return mapOf(
            Constant.XMETA_GEN to FolderType.NORMAL,
            Constant.XMETA_TEMP to FolderType.NORMAL,
        )
    }
}
