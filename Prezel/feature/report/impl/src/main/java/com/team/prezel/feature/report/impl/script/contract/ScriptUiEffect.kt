package com.team.prezel.feature.report.impl.script.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.report.impl.script.model.ScriptUiMessage

internal sealed interface ScriptUiEffect : UiEffect {
    data object NavigateToBack : ScriptUiEffect

    data class ShowMessage(
        val message: ScriptUiMessage,
    ) : ScriptUiEffect

    data class CurrentScriptCopyToClipBoard(
        val script: String,
    ) : ScriptUiEffect
}
