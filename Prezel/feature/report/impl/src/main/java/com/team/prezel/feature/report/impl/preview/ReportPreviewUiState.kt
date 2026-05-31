package com.team.prezel.feature.report.impl.preview

import com.team.prezel.core.model.practice.RecordingSpeed
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.feature.report.impl.contract.AnalysisReportUiState
import com.team.prezel.feature.report.impl.model.GrowthGraphData
import com.team.prezel.feature.report.impl.model.GrowthGraphItemUiModel
import com.team.prezel.feature.report.impl.model.PracticeRecordsUiModel
import com.team.prezel.feature.report.impl.model.PresentationInfoUiModel
import com.team.prezel.feature.report.impl.model.QuestionUiModel
import com.team.prezel.feature.report.impl.model.ScriptAnalysisGraphData
import com.team.prezel.feature.report.impl.model.SpeedGraphData
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate

private val ReportPreviewBaseUiState: AnalysisReportUiState.Content = AnalysisReportUiState.Content(
    presentationInfo = PresentationInfoUiModel(
        category = Category.EDUCATION,
        title = "분기 실적 공유",
        purpose = Purpose.INFO,
        style = Style.CALM,
        audience = Audience.PROFESSIONAL,
        analyzedAt = "2026-05-14",
        durationSeconds = 332,
    ),
    summaryFeedback = "전달은 안정적으로 잘 되고 있어요. 이제 속도와 리듬을 조금만 다듬어볼게요. 문장 흐름이 더 좋아지려면 문장 속에 키워드가 3개 이상 들어가지 않는 게 좋아요. 지금처럼만 하면 전달력은 계속 좋아질 수 있어요.",
    accuracyScore = 82.0,
    scriptMatchRate = 76.0,
    speedGraphData = SpeedGraphData(
        spm = 240,
        result = RecordingSpeed.ADEQUATE,
    ),
    growthGraphData = GrowthGraphData(
        items = persistentListOf(
            GrowthGraphItemUiModel(attempt = 1, accuracyScore = 52.0, scriptMatchRate = 48.0),
            GrowthGraphItemUiModel(attempt = 2, accuracyScore = 64.0, scriptMatchRate = 58.0),
            GrowthGraphItemUiModel(attempt = 3, accuracyScore = 72.0, scriptMatchRate = 66.0),
            GrowthGraphItemUiModel(attempt = 4, accuracyScore = 62.0, scriptMatchRate = 56.0),
            GrowthGraphItemUiModel(attempt = 5, accuracyScore = 72.0, scriptMatchRate = 71.0),
            GrowthGraphItemUiModel(attempt = 6, accuracyScore = 82.0, scriptMatchRate = 76.0),
        ),
        selectedItemIndex = 2,
    ),
    scriptAnalysisGraphData = ScriptAnalysisGraphData(
        spellingCount = 2,
        grammarCount = 1,
        totalErrorCount = 3,
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
    selfFeedback = "아 발표 드디어 끝났다",
    isPast = true,
    practiceRecords = PracticeRecordsUiModel(
        practicedDates = listOf(
            LocalDate(2026, 5, 10),
            LocalDate(2026, 5, 12),
            LocalDate(2026, 5, 13),
        ),
        startDate = LocalDate(2026, 5, 10),
        endDate = LocalDate(2026, 5, 14),
    ),
)

internal val ReportPreviewPastUiState: AnalysisReportUiState.Content = ReportPreviewBaseUiState

internal val ReportPreviewUpcomingUiState: AnalysisReportUiState.Content = ReportPreviewBaseUiState.copy(
    isPast = false,
    selfFeedback = null,
    practiceRecords = null,
)
