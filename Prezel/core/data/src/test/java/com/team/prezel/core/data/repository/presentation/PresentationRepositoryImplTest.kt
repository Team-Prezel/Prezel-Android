package com.team.prezel.core.data.repository.presentation

import com.team.prezel.core.common.error.AppError
import com.team.prezel.core.common.error.AppException
import com.team.prezel.core.data.repository.PresentationRepositoryImpl
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.PresentationAnalysisRequest
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.core.network.datasource.PresentationRemoteDataSource
import com.team.prezel.core.network.model.ApiException
import com.team.prezel.core.network.model.ServerErrorCode
import com.team.prezel.core.network.model.presentation.PresentationAnalysisRequestParts
import com.team.prezel.core.network.model.presentation.PresentationExpectedQuestionResponse
import com.team.prezel.core.network.model.presentation.PresentationGrowthResponse
import com.team.prezel.core.network.model.presentation.PresentationScriptAnalysisResponse
import com.team.prezel.core.network.model.presentation.PresentationScriptDetailResponse
import com.team.prezel.core.network.model.presentation.PresentationSummaryResponse
import com.team.prezel.core.network.model.presentation.PresentationWordAnalysisResponse
import com.team.prezel.core.network.model.presentation.PresentationWordDetailResponse
import kotlinx.datetime.LocalDateTime
import kotlinx.coroutines.runBlocking
import kotlin.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class PresentationRepositoryImplTest {
    @Test
    fun `발표 분석 요청을 서버 요청 형태로 전달하고 요약 응답으로 변환한다`() =
        runBlocking {
            val remoteDataSource = FakePresentationRemoteDataSource()
            val repository = PresentationRepositoryImpl(presentationRemoteDataSource = remoteDataSource)

            val result = repository
                .analyzePresentation(
                    PresentationAnalysisRequest(
                        title = "서비스 제안 발표",
                        dateTime = LocalDateTime.parse("2026-05-21T10:15:30"),
                        category = Category.PERSUASION,
                        purpose = Purpose.CONTENT_DELIVERY,
                        style = Style.PROFESSIONAL,
                        audience = Audience.GENERAL_AUDIENCE,
                        script = null,
                        audioFilePath = "/tmp/presentation.wav",
                    ),
                ).getOrThrow()

            assertEquals("서비스 제안 발표", remoteDataSource.analyzeRequest?.name)
            assertEquals("2026-05-21T10:15:30", remoteDataSource.analyzeRequest?.date)
            assertEquals("OFFER", remoteDataSource.analyzeRequest?.type)
            assertEquals("INFO", remoteDataSource.analyzeRequest?.purpose)
            assertEquals("FORMAL", remoteDataSource.analyzeRequest?.style)
            assertEquals("GENERAL", remoteDataSource.analyzeRequest?.audience)
            assertNull(remoteDataSource.analyzeRequest?.script)
            assertEquals("/tmp/presentation.wav", remoteDataSource.analyzeRequest?.audioFilePath)

            assertEquals(11L, result.presentationId)
            assertEquals(22L, result.analysisResultId)
            assertEquals(Category.REPORT, result.category)
            assertEquals(Purpose.IMPROVE_UNDERSTANDING, result.purpose)
            assertEquals(Style.COMFORTABLE, result.style)
            assertEquals(Audience.TEAMMATES, result.audience)
            assertEquals(Instant.parse("2026-05-20T01:02:03Z"), result.analyzedAt)
            assertEquals(2, result.growth.size)
            assertEquals("왜 이 전략이 필요한가요?", result.expectedQuestions.single().question)
        }

    @Test
    fun `타임존 없는 분석 시각도 파싱한다`() =
        runBlocking {
            val repository = PresentationRepositoryImpl(
                    presentationRemoteDataSource = FakePresentationRemoteDataSource(
                    summaryResponse = FakePresentationRemoteDataSource.summaryResponse(
                        analysisDate = "2026-05-21T23:29:46.974216441",
                    ),
                ),
            )

            val result = repository
                .analyzePresentation(
                    PresentationAnalysisRequest(
                        title = "서비스 제안 발표",
                        dateTime = LocalDateTime.parse("2026-05-21T10:15:30"),
                        category = Category.PERSUASION,
                        purpose = Purpose.CONTENT_DELIVERY,
                        style = Style.PROFESSIONAL,
                        audience = Audience.GENERAL_AUDIENCE,
                        script = null,
                        audioFilePath = "/tmp/presentation.wav",
                    ),
                ).getOrThrow()

            assertEquals(Instant.parse("2026-05-21T14:29:46.974216441Z"), result.analyzedAt)
        }

    @Test
    fun `재분석 요청은 presentationId와 audioFilePath를 그대로 전달한다`() =
        runBlocking {
            val remoteDataSource = FakePresentationRemoteDataSource()
            val repository = PresentationRepositoryImpl(presentationRemoteDataSource = remoteDataSource)

            val result = repository
                .reAnalyzePresentation(
                    presentationId = 91L,
                    audioFilePath = "/tmp/re-analyze.m4a",
                ).getOrThrow()

            assertEquals(91L, remoteDataSource.reAnalyzePresentationId)
            assertEquals("/tmp/re-analyze.m4a", remoteDataSource.reAnalyzeAudioFilePath)
            assertEquals(22L, result.analysisResultId)
        }

    @Test
    fun `스크립트와 단어 상세 응답을 domain 모델로 변환한다`() =
        runBlocking {
            val remoteDataSource = FakePresentationRemoteDataSource()
            val repository = PresentationRepositoryImpl(presentationRemoteDataSource = remoteDataSource)

            val scriptDetail = repository.fetchScriptDetail(analysisResultId = 7L).getOrThrow()
            val wordDetail = repository.fetchWordDetail(analysisResultId = 7L).getOrThrow()

            assertEquals(7L, remoteDataSource.scriptDetailAnalysisResultId)
            assertEquals(7L, remoteDataSource.wordDetailAnalysisResultId)
            assertEquals("원본 대본", scriptDetail.originalScript)
            assertEquals("SPELLING", scriptDetail.scriptCorrections.single().errorType)
            assertEquals("프레즐", wordDetail.wordDetails.single().word)
            assertEquals(98.7, wordDetail.wordDetails.single().accuracy)
        }

    @Test
    fun `발표 관련 서버 not found 에러는 domain not found로 매핑한다`() =
        runBlocking {
            val repository = PresentationRepositoryImpl(
                presentationRemoteDataSource = FakePresentationRemoteDataSource(
                    analyzeThrowable = ApiException(
                        status = 404,
                        errorCode = ServerErrorCode.PRESENTATION_NOT_FOUND,
                        message = "존재하지 않는 발표입니다.",
                    ),
                ),
            )

            val failure = repository
                .analyzePresentation(
                    PresentationAnalysisRequest(
                        title = "제목",
                        dateTime = LocalDateTime.parse("2026-05-21T10:15:30"),
                        category = Category.REPORT,
                        purpose = Purpose.CONTENT_DELIVERY,
                        style = Style.CALM,
                        audience = Audience.EXPERT,
                        script = "대본",
                        audioFilePath = "/tmp/a.wav",
                    ),
                ).exceptionOrNull()

            val exception = assertIs<AppException>(failure)
            assertEquals(AppError.NOT_FOUND, exception.error)
        }

    private class FakePresentationRemoteDataSource(
        private val analyzeThrowable: Throwable? = null,
        private val summaryResponse: PresentationSummaryResponse = summaryResponse(),
    ) : PresentationRemoteDataSource {
        var analyzeRequest: PresentationAnalysisRequestParts? = null
            private set
        var reAnalyzePresentationId: Long? = null
            private set
        var reAnalyzeAudioFilePath: String? = null
            private set
        var scriptDetailAnalysisResultId: Long? = null
            private set
        var wordDetailAnalysisResultId: Long? = null
            private set

        override suspend fun analyzePresentation(request: PresentationAnalysisRequestParts): PresentationSummaryResponse {
            analyzeThrowable?.let { throw it }
            analyzeRequest = request

            return summaryResponse
        }

        override suspend fun reAnalyzePresentation(
            presentationId: Long,
            audioFilePath: String,
        ): PresentationSummaryResponse {
            reAnalyzePresentationId = presentationId
            reAnalyzeAudioFilePath = audioFilePath

            return summaryResponse
        }

        override suspend fun getScriptDetail(analysisResultId: Long): PresentationScriptDetailResponse {
            scriptDetailAnalysisResultId = analysisResultId

            return PresentationScriptDetailResponse(
                presentationId = 3L,
                audioUrl = "https://example.com/audio.mp3",
                originalScript = "원본 대본",
                scriptDetails = listOf(
                    PresentationScriptAnalysisResponse(
                        errorType = "SPELLING",
                        sentence = "문장",
                        originalText = "프래즐",
                        correctedText = "프레즐",
                        reason = "맞춤법 오류",
                    ),
                ),
            )
        }

        override suspend fun getWordDetail(analysisResultId: Long): PresentationWordDetailResponse {
            wordDetailAnalysisResultId = analysisResultId

            return PresentationWordDetailResponse(
                presentationId = 3L,
                audioUrl = "https://example.com/audio.mp3",
                wordDetails = listOf(
                    PresentationWordAnalysisResponse(
                        word = "프레즐",
                        status = "GOOD",
                        description = "정확합니다.",
                        accuracy = 98.7,
                        startTimeMs = 120L,
                        endTimeMs = 480L,
                    ),
                ),
            )
        }

        override suspend fun deleteAnalysis(analysisResultId: Long) = Unit

        companion object {
            fun summaryResponse(
                analysisDate: String = "2026-05-20T01:02:03Z",
            ): PresentationSummaryResponse =
                PresentationSummaryResponse(
                presentationId = 11L,
                analysisResultId = 22L,
                name = "서비스 제안 발표",
                type = "WORK",
                purpose = "UNDERSTANDING",
                style = "CASUAL",
                audience = "TEAMMATE",
                analysisDate = analysisDate,
                durationSeconds = 183,
                formattedDuration = "03:03",
                spm = 210,
                speedEval = "적당해요",
                summaryFeedback = "핵심이 잘 전달됐어요.",
                accuracyScore = 91.2,
                scriptMatchRate = 88.4,
                spellErrorCount = 1,
                grammarErrorCount = 2,
                totalErrorCount = 3,
                growthGraph = listOf(
                    PresentationGrowthResponse(
                        attempt = 1,
                        accuracyScore = 70.0,
                        scriptMatchRate = 62.0,
                    ),
                    PresentationGrowthResponse(
                        attempt = 2,
                        accuracyScore = 91.2,
                        scriptMatchRate = 88.4,
                    ),
                ),
                expectedQuestions = listOf(
                    PresentationExpectedQuestionResponse(
                        question = "왜 이 전략이 필요한가요?",
                        answer = "시장 진입 속도를 높이기 위해서입니다.",
                    ),
                ),
            )
        }
    }
}
