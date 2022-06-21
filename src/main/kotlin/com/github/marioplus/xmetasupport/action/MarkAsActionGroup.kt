package com.github.marioplus.xmetasupport.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup

/**
 * 标记xmeta_gen目录
 *
 * @author marioplus
 * @since 1.0.0
 */
class MarkAsActionGroup : DefaultActionGroup() {

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        return arrayOf(
            MarkAsAction("动态菜单1"),
            MarkAsAction("动态菜单2"),
        )
    }
}