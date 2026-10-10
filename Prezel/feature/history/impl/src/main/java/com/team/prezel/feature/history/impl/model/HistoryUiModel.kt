package com.team.prezel.feature.history.impl.model

import androidx.compose.runtime.Immutable
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.PresentationAnalysisErrorType
import com.team.prezel.core.model.presentation.PresentationAnalysisStatus
import com.team.prezel.core.model.presentation.PresentationInfo
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

@Immutable
internal data class HistoryUiModel(
    val presentationId: Long,
    val title: String,
    val presentationDate: LocalDate,
    val category: Category,
    val dDay: String,
    val analysisStatus: PresentationAnalysisStatus = PresentationAnalysisStatus.DEFAULT,
    val analysisResultId: Long? = null,
    val analysisErrorType: PresentationAnalysisErrorType? = null,
    val script: String? = null,
    val audioUrl: String? = null,
) {
    val dateLabel: String = "%04d년 %02d월 %02d일".format(presentationDate.year, presentationDate.month.number, presentationDate.day)

    companion object {
        fun PresentationInfo.toUiModel(): HistoryUiModel =
            HistoryUiModel(
                presentationId = id,
                title = title,
                presentationDate = presentationDate,
                category = category,
                dDay = dDay,
                analysisStatus = analysisStatus,
                analysisResultId = analysisResultId,
                analysisErrorType = analysisErrorType,
                script = script,
                audioUrl = audioUrl,
            )
    }
}
