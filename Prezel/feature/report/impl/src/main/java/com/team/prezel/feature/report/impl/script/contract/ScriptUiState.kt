package com.team.prezel.feature.report.impl.script.contract

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.presentation.ScriptErrorType
import com.team.prezel.core.ui.base.UiState
import com.team.prezel.feature.report.impl.script.model.ScriptCorrectionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class ScriptUiState(
    val isLoading: Boolean = false,
    val originalScript: String = "",
    val currentScript: String = "",
    val selectedCorrectionId: Long? = null,
    val selectedCorrectionPopupY: Int = 0,
    val scriptDetails: ImmutableList<ScriptCorrectionUiModel> = persistentListOf(),
) : UiState {
    val unappliedSpellingErrors: Int =
        scriptDetails.count { detail ->
            detail.errorType == ScriptErrorType.SPELLING && !detail.isApplied
        }

    val unappliedGrammarErrors: Int =
        scriptDetails.count { detail ->
            detail.errorType == ScriptErrorType.GRAMMAR && !detail.isApplied
        }

    val selectedCorrection: ScriptCorrectionUiModel? =
        scriptDetails.firstOrNull { detail ->
            detail.id == selectedCorrectionId
        }

    val enabledAllCorrectionButton: Boolean =
        unappliedGrammarErrors + unappliedSpellingErrors > 0
}
