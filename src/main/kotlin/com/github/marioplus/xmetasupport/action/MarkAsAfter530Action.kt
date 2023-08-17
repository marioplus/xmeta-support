package com.github.marioplus.xmetasupport.action

import com.github.marioplus.xmetasupport.Constant
import com.github.marioplus.xmetasupport.enums.FolderType

/**
 * 5.3.0之后
 *
 * @author marioplus
 * @since 1.0.0
 */
class MarkAsAfter530Action : BaseMarkAsAction() {

    override fun getMarkAsFoldersDefinition(): Map<String, FolderType> {
        return mapOf(
            Constant.XMETA_GEN to FolderType.SOURCE,
            Constant.XMETA_TEMP to FolderType.GENERATED_SOURCE,
        )
    }
}
