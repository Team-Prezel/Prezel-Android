package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.presentation.GetMainDataResponse
import com.team.prezel.core.network.model.presentation.GetPracticeRecordsResponse
import com.team.prezel.core.network.model.presentation.GetPresentationDetailResponse
import com.team.prezel.core.network.model.presentation.GetPresentationsResponse
import com.team.prezel.core.network.model.presentation.PresentationScriptDetailResponse
import com.team.prezel.core.network.model.presentation.PresentationSummaryResponse
import com.team.prezel.core.network.model.presentation.PresentationWordDetailResponse
import com.team.prezel.core.network.model.presentation.review.SelfFeedbackRequest
import com.team.prezel.core.network.model.requireData
import com.team.prezel.core.network.model.requireSuccess
import com.team.prezel.core.network.service.PresentationService
import io.ktor.client.request.forms.ChannelProvider
import io.ktor.client.request.forms.FormBuilder
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import java.io.File
import javax.inject.Inject

internal class PresentationRemoteDataSourceImpl @Inject constructor(
    private val presentationService: PresentationService,
) : PresentationRemoteDataSource {
    override suspend fun analyzePresentation(
        name: String,
        date: String,
        type: String,
        purpose: String,
        style: String,
        audience: String,
        script: String?,
        scriptFilePath: String?,
        audioFilePath: String,
    ): PresentationSummaryResponse {
        require(audioFilePath.isNotBlank()) { "발표 음성 파일 경로가 비어 있습니다." }

        val multipart = MultiPartFormDataContent(
            formData {
                append("name", name)
                append("date", date)
                append("type", type)
                append("purpose", purpose)
                append("style", style)
                append("audience", audience)
                appendAudioPart(audioFilePath = audioFilePath)
                script?.takeIf(String::isNotBlank)?.let { append("script", it) }
                scriptFilePath?.takeIf(String::isNotBlank)?.let { path ->
                    appendScriptPart(scriptFilePath = path)
                }
            },
        )

        return presentationService
            .analyzePresentation(multipart = multipart)
            .requireData()
    }

    override suspend fun reAnalyzePresentation(
        presentationId: Long,
        script: String?,
        scriptFilePath: String?,
        audioFilePath: String,
    ): PresentationSummaryResponse {
        require(audioFilePath.isNotBlank()) { "발표 음성 파일 경로가 비어 있습니다." }

        val multipart = MultiPartFormDataContent(
            formData {
                appendAudioPart(audioFilePath = audioFilePath)
                script?.takeIf(String::isNotBlank)?.let { append("script", it) }
                scriptFilePath?.takeIf(String::isNotBlank)?.let { path ->
                    appendScriptPart(scriptFilePath = path)
                }
            },
        )

        return presentationService
            .reAnalyzePresentation(
                presentationId = presentationId,
                multipart = multipart,
            ).requireData()
    }

    override suspend fun getScriptDetail(analysisResultId: Long): PresentationScriptDetailResponse =
        presentationService.getScriptDetail(analysisResultId = analysisResultId).requireData()

    override suspend fun getWordDetail(analysisResultId: Long): PresentationWordDetailResponse =
        presentationService.getWordDetail(analysisResultId = analysisResultId).requireData()

    override suspend fun deleteAnalysis(analysisResultId: Long) {
        presentationService.deleteAnalysis(analysisResultId = analysisResultId).requireSuccess()
    }

    override suspend fun getUpcomingPresentations(): List<GetPresentationsResponse> = presentationService.getUpcomingPresentations().requireData()

    override suspend fun getPastPresentations(): List<GetPresentationsResponse> = presentationService.getPastPresentations().requireData()

    override suspend fun getUpcomingPresentationDetail(presentationId: Long): PresentationSummaryResponse =
        presentationService.getUpcomingPresentationDetail(presentationId = presentationId).requireData().toPresentationSummaryResponse()

    override suspend fun getPastPresentationDetail(presentationId: Long): PresentationSummaryResponse =
        presentationService.getPastPresentationDetail(presentationId = presentationId).requireData().toPresentationSummaryResponse()

    override suspend fun getPracticeRecords(presentationId: Long): GetPracticeRecordsResponse =
        presentationService.getPracticeRecords(presentationId = presentationId).requireData()

    override suspend fun getMainData(): List<GetMainDataResponse> = presentationService.getMainData().requireData()

    override suspend fun writeSelfFeedback(
        presentationId: Long,
        content: String,
    ) {
        presentationService
            .writeSelfFeedback(
                presentationId = presentationId,
                request = SelfFeedbackRequest(content = content),
            ).requireSuccess()
    }
}

private fun FormBuilder.appendAudioPart(audioFilePath: String) {
    val audioFile = File(audioFilePath)
    append(
        key = "audio",
        value = audioFile.toChannelProvider(),
        headers = Headers.build {
            append(HttpHeaders.ContentType, "audio/${audioFile.extension}")
            append(HttpHeaders.ContentDisposition, "filename=\"${audioFile.name}\"")
        },
    )
}

private fun GetPresentationDetailResponse.toPresentationSummaryResponse(): PresentationSummaryResponse =
    analysisResult.copy(
        reviewContent = reviewContent ?: analysisResult.reviewContent,
    )

private fun FormBuilder.appendScriptPart(scriptFilePath: String) {
    val file = File(scriptFilePath)

    append(
        key = "scriptFile",
        value = file.toChannelProvider(),
        headers = Headers.build {
            append(HttpHeaders.ContentType, "text/${file.extension}")
            append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
        },
    )
}

private fun File.toChannelProvider(): ChannelProvider =
    ChannelProvider(size = length()) {
        require(exists()) { "파일이 존재하지 않습니다: $path" }
        require(canRead()) { "파일을 읽을 수 없습니다: $path" }
        inputStream().toByteReadChannel()
    }
