package com.team.prezel.feature.report.impl.detail.preview

import com.team.prezel.core.model.practice.RecordingSpeed
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.feature.report.impl.analysis.contract.AnalysisReportUiState
import com.team.prezel.feature.report.impl.detail.model.ImprovementGraphData
import com.team.prezel.feature.report.impl.detail.model.ImprovementGraphItemUiModel
import com.team.prezel.feature.report.impl.detail.model.PracticeUiModel
import com.team.prezel.feature.report.impl.detail.model.PresentationInfoUiModel
import com.team.prezel.feature.report.impl.detail.model.QuestionUiModel
import com.team.prezel.feature.report.impl.detail.model.ReportDetailUiModel
import com.team.prezel.feature.report.impl.detail.model.ScriptAnalysisGraphData
import com.team.prezel.feature.report.impl.detail.model.SpeedGraphData
import com.team.prezel.feature.report.impl.history.contract.HistoryReportUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate

internal object ReportDetailPreviewData {
    val reportDetail =
        ReportDetailUiModel(
            presentationInfo = PresentationInfoUiModel(
                category = Category.EDUCATION,
                title = "분기 실적 공유",
                purpose = Purpose.INFO,
                style = Style.CALM,
                audience = Audience.PROFESSIONAL,
                analyzedAt = "2026-05-14",
                durationSeconds = 332,
            ),
            summaryFeedback = "핵심 메시지는 명확했지만 전달 속도 편차가 있었습니다.",
            accuracyScore = 82.0,
            scriptMatchRate = 76.0,
            speedGraphData = SpeedGraphData(
                spm = 240,
                result = RecordingSpeed.ADEQUATE,
            ),
            improvementGraphData = ImprovementGraphData(
                items = persistentListOf(
                    ImprovementGraphItemUiModel(attempt = 1, accuracyScore = 52.0, scriptMatchRate = 48.0),
                    ImprovementGraphItemUiModel(attempt = 2, accuracyScore = 64.0, scriptMatchRate = 58.0),
                    ImprovementGraphItemUiModel(attempt = 3, accuracyScore = 72.0, scriptMatchRate = 66.0),
                    ImprovementGraphItemUiModel(attempt = 4, accuracyScore = 62.0, scriptMatchRate = 56.0),
                    ImprovementGraphItemUiModel(attempt = 5, accuracyScore = 72.0, scriptMatchRate = 71.0),
                    ImprovementGraphItemUiModel(attempt = 6, accuracyScore = 82.0, scriptMatchRate = 76.0),
                ),
                selectedItemIndex = 2,
            ),
            scriptAnalysisGraphData = ScriptAnalysisGraphData(
                spellingCount = 2,
                grammarCount = 1,
            ),
            expectedQuestions = persistentListOf(
                QuestionUiModel(
                    question = "핵심 질문",
                    answer = "결론을 먼저 배치해 이해도를 높이세요.",
                ),
                QuestionUiModel(
                    question = "수치 근거는 충분한가요?",
                    answer = "슬라이드마다 핵심 지표를 한 줄로 다시 강조해 주세요.",
                ),
            ),
        )

    val analysisState: AnalysisReportUiState.Content =
        AnalysisReportUiState.Content(
            reportDetail = reportDetail,
        )

    val historyState: HistoryReportUiState.Content =
        HistoryReportUiState.Content(
            reportDetail = reportDetail,
            selfFeedback = "도입은 안정적이었지만 질의응답에서 답변 길이가 길어졌습니다.",
            practices = persistentListOf(
                PracticeUiModel(date = LocalDate(2026, 5, 10), isPracticed = true),
                PracticeUiModel(date = LocalDate(2026, 5, 11), isPracticed = false),
                PracticeUiModel(date = LocalDate(2026, 5, 12), isPracticed = true),
                PracticeUiModel(date = LocalDate(2026, 5, 13), isPracticed = true),
            ),
        )
}
