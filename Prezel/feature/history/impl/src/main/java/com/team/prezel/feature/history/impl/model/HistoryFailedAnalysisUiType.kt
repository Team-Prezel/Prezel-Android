package com.team.prezel.feature.history.impl.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.team.prezel.core.model.presentation.PresentationAnalysisErrorType
import com.team.prezel.feature.history.impl.R
import com.team.prezel.core.ui.R as CoreUiR

internal sealed interface HistoryFailedAnalysisUiType {
    @get:StringRes
    val titleResId: Int

    @get:StringRes
    val descriptionResId: Int

    @get:DrawableRes
    val visualResId: Int

    val script: String?

    val reusableAudioUrl: String?
        get() = null

    data class VoiceRecognition(
        override val script: String?,
    ) : HistoryFailedAnalysisUiType {
        override val titleResId: Int = R.string.feature_history_impl_voice_recognition_failed_title
        override val descriptionResId: Int = R.string.feature_history_impl_voice_recognition_failed_description
        override val visualResId: Int = CoreUiR.drawable.core_ui_error_voice
    }

    data class FileRecognition(
        override val script: String?,
    ) : HistoryFailedAnalysisUiType {
        override val titleResId: Int = R.string.feature_history_impl_file_recognition_failed_title
        override val descriptionResId: Int = R.string.feature_history_impl_file_recognition_failed_description
        override val visualResId: Int = CoreUiR.drawable.core_ui_error_voice
    }

    data class Analysis(
        override val script: String?,
        override val reusableAudioUrl: String?,
    ) : HistoryFailedAnalysisUiType {
        override val titleResId: Int = R.string.feature_history_impl_analysis_failed_title
        override val descriptionResId: Int = R.string.feature_history_impl_analysis_failed_description
        override val visualResId: Int = CoreUiR.drawable.core_ui_error_analyze
    }

    data class Unknown(
        val rawErrorType: String?,
        override val script: String?,
        override val reusableAudioUrl: String?,
    ) : HistoryFailedAnalysisUiType {
        override val titleResId: Int = R.string.feature_history_impl_analysis_failed_title
        override val descriptionResId: Int = R.string.feature_history_impl_analysis_failed_description
        override val visualResId: Int = CoreUiR.drawable.core_ui_error_analyze
    }

    companion object {
        fun from(
            value: String?,
            script: String?,
            audioUrl: String?,
        ): HistoryFailedAnalysisUiType =
            when {
                value.equals(PresentationAnalysisErrorType.VOICE_RECOGNITION.value, ignoreCase = true) -> {
                    VoiceRecognition(script = script)
                }

                value.equals(PresentationAnalysisErrorType.FILE_RECOGNITION.value, ignoreCase = true) -> {
                    FileRecognition(script = script)
                }

                value.equals(PresentationAnalysisErrorType.ANALYSIS.value, ignoreCase = true) -> {
                    Analysis(script = script, reusableAudioUrl = audioUrl)
                }

                else -> {
                    Unknown(
                        rawErrorType = value,
                        script = script,
                        reusableAudioUrl = audioUrl,
                    )
                }
            }
    }
}
