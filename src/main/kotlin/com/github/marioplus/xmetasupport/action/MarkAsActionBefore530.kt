package com.github.marioplus.xmetasupport.action

import com.github.marioplus.xmetasupport.Constant

/**
 * 5.3.0之前
 *
 * @author marioplus
 * @since 1.0.0
 */
class MarkAsActionBefore530 : BaseMarkAsAction() {
    override fun getMarkAsFoldersDefinition(): Map<String, FolderType> {
        return mapOf(
            Constant.X_META_GEN to FolderType.GENERATED_SOURCE,
            Constant.X_META_TEMP to FolderType.NORMAL,
        )
    }
}