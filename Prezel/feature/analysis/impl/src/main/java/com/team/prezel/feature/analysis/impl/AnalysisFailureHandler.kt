package com.team.prezel.feature.analysis.impl

import com.team.prezel.core.common.error.AppError
import com.team.prezel.core.common.error.AppException
import com.team.prezel.feature.analysis.impl.contract.AnalysisUploadType
import com.team.prezel.feature.analysis.impl.model.AnalysisUiMessage

internal sealed interface AnalysisFailureAction {
    data class RetryFileUpload(
        val uploadType: AnalysisUploadType,
    ) : AnalysisFailureAction

    data class ShowMessage(
        val message: AnalysisUiMessage,
    ) : AnalysisFailureAction
}

internal fun Throwable.toAnalysisFailureAction(): AnalysisFailureAction {
    val error = (this as? AppException)?.error

    return when (error) {
        AppError.INVALID_REQUEST,
        AppError.VOICE_RECOGNITION_FAILED,
        -> AnalysisFailureAction.RetryFileUpload(uploadType = AnalysisUploadType.AUDIO)

        AppError.UNAUTHORIZED -> AnalysisFailureAction.ShowMessage(message = AnalysisUiMessage.AUTH_EXPIRED)
        AppError.SERVER_ERROR -> AnalysisFailureAction.ShowMessage(message = AnalysisUiMessage.ANALYSIS_FAILED)
        AppError.NETWORK -> AnalysisFailureAction.ShowMessage(message = AnalysisUiMessage.NETWORK_FAILED)

        AppError.NOT_FOUND,
        AppError.DUPLICATE,
        AppError.UNKNOWN,
        null,
        -> AnalysisFailureAction.ShowMessage(message = AnalysisUiMessage.UNKNOWN_FAILED)
    }
}
