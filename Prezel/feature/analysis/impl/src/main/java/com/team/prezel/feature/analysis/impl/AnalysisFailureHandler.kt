package com.team.prezel.feature.analysis.impl

import com.team.prezel.core.common.error.AppError
import com.team.prezel.core.common.error.AppException
import com.team.prezel.feature.analysis.impl.contract.AnalysisUploadType
import com.team.prezel.feature.analysis.impl.model.AnalysisUiMessage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import java.net.SocketTimeoutException

internal sealed interface AnalysisFailureAction {
    data object RetryAnalysis : AnalysisFailureAction

    data object NavigateHome : AnalysisFailureAction

    data class RetryFileUpload(
        val uploadType: AnalysisUploadType,
    ) : AnalysisFailureAction

    data class ShowMessage(
        val message: AnalysisUiMessage,
    ) : AnalysisFailureAction
}

internal fun Throwable.toAnalysisFailureAction(): AnalysisFailureAction {
    if (isTimeoutException()) {
        return AnalysisFailureAction.NavigateHome
    }

    if (this is CancellationException) {
        return AnalysisFailureAction.RetryAnalysis
    }

    val error = (this as? AppException)?.error

    return when (error) {
        AppError.INVALID_REQUEST,
        -> AnalysisFailureAction.RetryFileUpload(uploadType = AnalysisUploadType.AUDIO)

        AppError.SCRIPT_FILE_RECOGNITION_FAILED -> AnalysisFailureAction.RetryFileUpload(uploadType = AnalysisUploadType.SCRIPT)

        AppError.UNAUTHORIZED -> AnalysisFailureAction.ShowMessage(message = AnalysisUiMessage.AUTH_EXPIRED)
        AppError.VOICE_RECOGNITION_FAILED -> AnalysisFailureAction.RetryFileUpload(uploadType = AnalysisUploadType.AUDIO)

        AppError.VOICE_ANALYSIS_FAILED,
        AppError.SERVER_ERROR,
        AppError.NETWORK,
        -> AnalysisFailureAction.RetryAnalysis

        AppError.NOT_FOUND,
        AppError.DUPLICATE,
        AppError.UNKNOWN,
        null,
        -> AnalysisFailureAction.ShowMessage(message = AnalysisUiMessage.UNKNOWN_FAILED)
    }
}

private fun Throwable.isTimeoutException(): Boolean =
    this is TimeoutCancellationException ||
        this is SocketTimeoutException ||
        generateSequence(this) { throwable -> throwable.cause }
            .any { throwable -> throwable::class.java.simpleName == "HttpRequestTimeoutException" }
