package com.github.marioplus.xmetasupport.action

import com.github.marioplus.xmetasupport.Constant
import com.github.marioplus.xmetasupport.enums.FolderType

/**
 * 5.3.0之前
 *
 * @author marioplus
 * @since 1.0.0
 */
class MarkAsBefore530Action : BaseMarkAsAction() {
    override fun getMarkAsFoldersDefinition(): Map<String, FolderType> {
        return mapOf(
            Constant.XMETA_GEN to FolderType.GENERATED_SOURCE,
            Constant.XMETA_TEMP to FolderType.NORMAL,
        )
    }
}
