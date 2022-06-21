package com.github.marioplus.xmetasupport.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.NlsActions
import java.util.Objects

/**
 * 标记xmeta_gen目录为
 *
 * @author marioplus
 * @since 1.0.0
 */
class MarkAsAction : AnAction {

    constructor() : super()

    constructor(text: @NlsActions.ActionText String) : super(text)

    override fun actionPerformed(event: AnActionEvent) {
        event.project
        var message: String = "${event.presentation.text} Selected!"
        event.getData(CommonDataKeys.NAVIGATABLE)
            ?.apply {
                message += "\nSelected Element: $this"
            }
        Messages.showMessageDialog(
            event.project,
            message,
            event.presentation.text,
            Messages.getInformationIcon()
        )
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible = Objects.nonNull(event.project)
    }
}