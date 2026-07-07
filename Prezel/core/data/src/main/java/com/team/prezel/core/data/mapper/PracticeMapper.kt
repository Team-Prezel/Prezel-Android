package com.team.prezel.core.data.mapper

import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult
import com.team.prezel.core.model.practice.PracticeRecordingOverallEvaluation
import com.team.prezel.core.model.practice.PracticeScript
import com.team.prezel.core.model.practice.RecordingSpeed
import com.team.prezel.core.network.model.practice.AnalyzePracticeRecordingResponse
import com.team.prezel.core.network.model.practice.PracticeSentenceResponse
import kotlin.math.roundToInt

internal fun PracticeSentenceResponse.toDomain(): PracticeScript =
    PracticeScript(
        id = PRACTICE_SCRIPT_ID,
        content = sentence,
    )

internal fun AnalyzePracticeRecordingResponse.toDomain(): PracticeRecordingAnalysisResult =
    PracticeRecordingAnalysisResult(
        pronunciationScore = accuracyScore.roundToInt(),
        speed = RecordingSpeed.from(value = speedEvaluation),
        overallEvaluation = PracticeRecordingOverallEvaluation.from(overallEvaluation),
    )

private const val PRACTICE_SCRIPT_ID = 0L
