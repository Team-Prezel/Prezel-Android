package com.team.prezel.core.data.mapper

import com.team.prezel.core.common.error.AppError
import com.team.prezel.core.common.error.AppException
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
    validateVoiceRecognition().let { response ->
        PracticeRecordingAnalysisResult(
            pronunciationScore = response.accuracyScore.roundToInt(),
            speed = RecordingSpeed.from(value = response.speedEvaluation),
            overallEvaluation = PracticeRecordingOverallEvaluation.from(response.overallEvaluation),
        )
    }

private fun AnalyzePracticeRecordingResponse.validateVoiceRecognition(): AnalyzePracticeRecordingResponse =
    apply {
        if (accuracyScore.roundToInt() == VOICE_RECOGNITION_FAILED_SCORE) {
            throw AppException(
                error = AppError.VOICE_RECOGNITION_FAILED,
                message = "분석할 음성 인식 실패",
            )
        }
    }

private const val PRACTICE_SCRIPT_ID = 0L
private const val VOICE_RECOGNITION_FAILED_SCORE = 0
