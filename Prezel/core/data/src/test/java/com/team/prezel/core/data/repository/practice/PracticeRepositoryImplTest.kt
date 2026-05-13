package com.team.prezel.core.data.repository.practice

import com.team.prezel.core.data.repository.PracticeRepositoryImpl
import com.team.prezel.core.model.practice.PracticeRecordingSpeed
import com.team.prezel.core.network.datasource.PracticeRemoteDataSource
import com.team.prezel.core.network.model.practice.AnalyzePracticeRecordingResponse
import com.team.prezel.core.network.model.practice.PracticeSentenceResponse
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class PracticeRepositoryImplTest {
    @Test
    fun `연습 녹음 대본을 조회한다`() =
        runBlocking {
            val repository = PracticeRepositoryImpl(
                practiceRemoteDataSource = FakePracticeRemoteDataSource(
                    sentence = "간장 공장 공장장은 강 공장장이다.",
                ),
            )

            val result = repository.fetchPracticeScript().getOrThrow()

            assertEquals(0L, result.id)
            assertEquals("간장 공장 공장장은 강 공장장이다.", result.content)
        }

    @Test
    fun `연습 녹음 분석을 요청하고 분석 결과로 변환한다`() =
        runBlocking {
            val remoteDataSource = FakePracticeRemoteDataSource(
                sentence = "간장 공장 공장장은 강 공장장이다.",
            )
            val repository = PracticeRepositoryImpl(
                practiceRemoteDataSource = remoteDataSource,
            )

            val result = repository
                .analyzePracticeRecording(
                    recordingFilePath = "/tmp/practice.wav",
                    referenceText = "간장 공장 공장장은 강 공장장이다.",
                ).getOrThrow()

            assertEquals("/tmp/practice.wav", remoteDataSource.analyzeRecordingFilePath)
            assertEquals("간장 공장 공장장은 강 공장장이다.", remoteDataSource.analyzeReferenceText)
            assertEquals(86, result.pronunciationScore)
            assertEquals(PracticeRecordingSpeed.ADEQUATE, result.speed)
        }

    private class FakePracticeRemoteDataSource(
        private val sentence: String,
    ) : PracticeRemoteDataSource {
        var analyzeRecordingFilePath: String? = null
            private set
        var analyzeReferenceText: String? = null
            private set

        override suspend fun getPracticeSentence(): PracticeSentenceResponse = PracticeSentenceResponse(sentence = sentence)

        override suspend fun analyzePracticeRecording(
            recordingFilePath: String,
            referenceText: String,
        ): AnalyzePracticeRecordingResponse {
            analyzeRecordingFilePath = recordingFilePath
            analyzeReferenceText = referenceText

            return AnalyzePracticeRecordingResponse(
                accuracyScore = 85.5,
                speedEvaluation = "적당해요",
                overallEvaluation = "Good",
            )
        }
    }
}
