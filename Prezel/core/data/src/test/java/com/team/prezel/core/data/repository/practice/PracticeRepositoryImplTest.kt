package com.team.prezel.core.data.repository.practice

import com.team.prezel.core.data.repository.PracticeRepositoryImpl
import com.team.prezel.core.model.practice.PracticeRecordingOverallEvaluation
import com.team.prezel.core.model.practice.PracticeRecordingSpeed
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.core.network.datasource.PracticeRemoteDataSource
import com.team.prezel.core.network.model.practice.AnalyzePracticeRecordingResponse
import com.team.prezel.core.network.model.practice.PracticeSentenceResponse
import com.team.prezel.core.network.model.practice.PresentationAnalysisAudience
import com.team.prezel.core.network.model.practice.PresentationAnalysisPurpose
import com.team.prezel.core.network.model.practice.PresentationAnalysisStyle
import com.team.prezel.core.network.model.practice.PresentationAnalysisType
import com.team.prezel.core.network.model.practice.PresentationRecordingAnalysisResponse
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
            assertEquals(PracticeRecordingOverallEvaluation.GOOD, result.overallEvaluation)
        }

    @Test
    fun `발표 녹음 분석을 요청하고 분석 결과로 변환한다`() =
        runBlocking {
            val remoteDataSource = FakePracticeRemoteDataSource(
                sentence = "간장 공장 공장장은 강 공장장이다.",
            )
            val repository = PracticeRepositoryImpl(
                practiceRemoteDataSource = remoteDataSource,
            )

            val result = repository
                .analyzePresentationRecording(
                    name = "중간 발표",
                    date = "2026-05-24",
                    category = Category.REPORT,
                    purpose = Purpose.IMPROVE_UNDERSTANDING,
                    style = Style.COMFORTABLE,
                    audience = Audience.TEAMMATES,
                    script = "발표 대본입니다.",
                    scriptFilePath = "/tmp/script.txt",
                    audioFilePath = "/tmp/audio.m4a",
                ).getOrThrow()

            assertEquals("중간 발표", remoteDataSource.presentationAnalysisName)
            assertEquals("2026-05-24", remoteDataSource.presentationAnalysisDate)
            assertEquals(PresentationAnalysisType.WORK, remoteDataSource.presentationAnalysisType)
            assertEquals(PresentationAnalysisPurpose.UNDERSTANDING, remoteDataSource.presentationAnalysisPurpose)
            assertEquals(PresentationAnalysisStyle.CASUAL, remoteDataSource.presentationAnalysisStyle)
            assertEquals(PresentationAnalysisAudience.TEAMMATE, remoteDataSource.presentationAnalysisAudience)
            assertEquals("발표 대본입니다.", remoteDataSource.presentationAnalysisScript)
            assertEquals("/tmp/script.txt", remoteDataSource.presentationAnalysisScriptFilePath)
            assertEquals("/tmp/audio.m4a", remoteDataSource.presentationAnalysisAudioFilePath)
            assertEquals(1L, result.presentationId)
            assertEquals(2L, result.analysisResultId)
            assertEquals("중간 발표", result.name)
            assertEquals(123, result.durationSeconds)
            assertEquals(145, result.spm)
            assertEquals(92.5, result.accuracyScore)
            assertEquals(88.0, result.scriptMatchRate)
            assertEquals(3, result.totalErrorCount)
            assertEquals("요약 피드백", result.summaryFeedback)
            assertEquals("예상 질문", result.expectedQuestions.single().question)
        }

    private class FakePracticeRemoteDataSource(
        private val sentence: String,
    ) : PracticeRemoteDataSource {
        var analyzeRecordingFilePath: String? = null
            private set
        var analyzeReferenceText: String? = null
            private set
        var presentationAnalysisName: String? = null
            private set
        var presentationAnalysisDate: String? = null
            private set
        var presentationAnalysisType: PresentationAnalysisType? = null
            private set
        var presentationAnalysisPurpose: PresentationAnalysisPurpose? = null
            private set
        var presentationAnalysisStyle: PresentationAnalysisStyle? = null
            private set
        var presentationAnalysisAudience: PresentationAnalysisAudience? = null
            private set
        var presentationAnalysisScript: String? = null
            private set
        var presentationAnalysisScriptFilePath: String? = null
            private set
        var presentationAnalysisAudioFilePath: String? = null
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

        override suspend fun analyzePresentationRecording(
            name: String,
            date: String,
            type: PresentationAnalysisType,
            purpose: PresentationAnalysisPurpose,
            style: PresentationAnalysisStyle,
            audience: PresentationAnalysisAudience,
            script: String?,
            scriptFilePath: String?,
            audioFilePath: String,
        ): PresentationRecordingAnalysisResponse {
            presentationAnalysisName = name
            presentationAnalysisDate = date
            presentationAnalysisType = type
            presentationAnalysisPurpose = purpose
            presentationAnalysisStyle = style
            presentationAnalysisAudience = audience
            presentationAnalysisScript = script
            presentationAnalysisScriptFilePath = scriptFilePath
            presentationAnalysisAudioFilePath = audioFilePath

            return PresentationRecordingAnalysisResponse(
                presentationId = 1,
                analysisResultId = 2,
                name = "중간 발표",
                type = "WORK",
                purpose = "UNDERSTANDING",
                style = "CASUAL",
                audience = "TEAMMATE",
                analysisDate = "2026-05-24",
                durationSeconds = 123,
                formattedDuration = "02:03",
                spm = 145,
                speedEval = "적당해요",
                summaryFeedback = "요약 피드백",
                accuracyScore = 92.5,
                scriptMatchRate = 88.0,
                spellErrorCount = 1,
                grammarErrorCount = 2,
                totalErrorCount = 3,
                growthGraph = listOf(
                    PresentationRecordingAnalysisResponse.GrowthGraph(
                        attempt = 1,
                        accuracyScore = 92.5,
                        scriptMatchRate = 88.0,
                    ),
                ),
                expectedQuestions = listOf(
                    PresentationRecordingAnalysisResponse.ExpectedQuestion(
                        question = "예상 질문",
                        answer = "예상 답변",
                    ),
                ),
            )
        }
    }
}
