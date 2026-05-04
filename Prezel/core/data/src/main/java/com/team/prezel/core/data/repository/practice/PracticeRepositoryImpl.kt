package com.team.prezel.core.data.repository.practice

import com.team.prezel.core.domain.repository.practice.PracticeRepository
import com.team.prezel.core.model.practice.PracticeRecordingAnalysisResult
import com.team.prezel.core.model.practice.PracticeRecordingSpeed
import com.team.prezel.core.model.practice.PracticeRecordingUpload
import com.team.prezel.core.model.practice.PracticeScript
import kotlinx.coroutines.delay
import javax.inject.Inject

internal class PracticeRepositoryImpl @Inject constructor() : PracticeRepository {
    override suspend fun fetchPracticeScript(): Result<PracticeScript> =
        runCatching {
            delay(FAKE_API_DELAY_MILLIS)
            fakePracticeScripts.random()
        }

    override suspend fun uploadPracticeRecording(recordingFilePath: String): Result<PracticeRecordingUpload> =
        runCatching {
            delay(FAKE_API_DELAY_MILLIS)
            PracticeRecordingUpload(id = FAKE_RECORDING_ID)
        }

    override suspend fun fetchPracticeRecordingAnalysisResult(recordingId: Long): Result<PracticeRecordingAnalysisResult> =
        runCatching {
            delay(FAKE_API_DELAY_MILLIS)
            PracticeRecordingAnalysisResult(
                pronunciationScore = 90,
                speed = PracticeRecordingSpeed.ADEQUATE,
            )
        }

    private companion object {
        const val FAKE_API_DELAY_MILLIS = 300L
        const val FAKE_RECORDING_ID = 1L

        val fakePracticeScripts = listOf(
            PracticeScript(
                id = 1L,
                content = "내가 그린 기린 그림은 잘 그린 기린 그림이고,\n네가 그린 기린 그림은 잘못 그린 기린 그림이다.",
            ),
            PracticeScript(
                id = 2L,
                content = "간장 공장 공장장은 강 공장장이고,\n된장 공장 공장장은 공 공장장이다.",
            ),
            PracticeScript(
                id = 3L,
                content = "저기 있는 말뚝이 말 맬 말뚝이냐,\n말 못 맬 말뚝이냐.",
            ),
            PracticeScript(
                id = 4L,
                content = "서울특별시 특허허가과 허가과장 허 과장.",
            ),
            PracticeScript(
                id = 5L,
                content = "신진 샹송 가수의 신춘 샹송 쇼.",
            ),
            PracticeScript(
                id = 6L,
                content = "작년에 온 솥 장수는 새 솥 장수이고,\n금년에 온 솥 장수는 헌 솥 장수이다.",
            ),
            PracticeScript(
                id = 7L,
                content = "상표 붙인 큰 깡통은 깐 깡통인가,\n안 깐 깡통인가.",
            ),
        )
    }
}
