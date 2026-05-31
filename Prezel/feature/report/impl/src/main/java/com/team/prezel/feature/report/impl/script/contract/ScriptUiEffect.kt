package com.team.prezel.feature.report.impl.script.contract

import com.team.prezel.core.ui.base.UiEffect

internal sealed interface ScriptUiEffect : UiEffect {
    data class CurrentScriptCopyToClipBoard(
        val script: String,
    ) : ScriptUiEffect
}
