package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.presentation.PresentationScriptDetailResponse
import com.team.prezel.core.network.model.presentation.PresentationSummaryResponse
import com.team.prezel.core.network.model.presentation.PresentationWordDetailResponse
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
                script?.takeIf(String::isNotBlank)?.let { append("script", it) }
                appendAudioPart(audioFilePath = audioFilePath)
                scriptFilePath?.let { path ->
                    appendTextPart(scriptFilePath = path)
                }
            },
        )

        return presentationService
            .analyzePresentation(multipart = multipart)
            .requireData()
    }

    override suspend fun reAnalyzePresentation(
        presentationId: Long,
        audioFilePath: String,
    ): PresentationSummaryResponse =
        presentationService
            .reAnalyzePresentation(
                presentationId = presentationId,
                multipart = audioFilePath.toAudioMultipart(),
            ).requireData()

    override suspend fun getScriptDetail(analysisResultId: Long): PresentationScriptDetailResponse =
        presentationService.getScriptDetail(analysisResultId = analysisResultId).requireData()

    override suspend fun getWordDetail(analysisResultId: Long): PresentationWordDetailResponse =
        presentationService.getWordDetail(analysisResultId = analysisResultId).requireData()

    override suspend fun deleteAnalysis(analysisResultId: Long) {
        presentationService.deleteAnalysis(analysisResultId = analysisResultId).requireSuccess()
    }

    override suspend fun getUpcomingPresentationDetail(presentationId: Long): PresentationSummaryResponse =
        presentationService.getUpcomingPresentationDetail(presentationId = presentationId).requireData().analysisResult

    private fun String.toAudioMultipart(): MultiPartFormDataContent =
        MultiPartFormDataContent(
            formData {
                appendAudioPart(this@toAudioMultipart)
            },
        )

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

    private fun FormBuilder.appendTextPart(scriptFilePath: String) {
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
            inputStream().toByteReadChannel()
        }
}
