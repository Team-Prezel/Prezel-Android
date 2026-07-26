package com.team.prezel.feature.report.impl.script.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface ScriptUiIntent : UiIntent {
    data object ClickClose : ScriptUiIntent

    data object ClickCopy : ScriptUiIntent

    data class ClickCorrection(
        val correctionId: Long,
        val popupY: Int,
    ) : ScriptUiIntent

    data object DismissCorrectionPopup : ScriptUiIntent

    data class ApplyCorrection(
        val correctionId: Long,
    ) : ScriptUiIntent

    data object ApplyAllCorrections : ScriptUiIntent
}
