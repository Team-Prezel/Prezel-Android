package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.presentation.PresentationAnalysisRequestParts
import com.team.prezel.core.network.model.presentation.PresentationScriptDetailResponse
import com.team.prezel.core.network.model.presentation.PresentationSummaryResponse
import com.team.prezel.core.network.model.presentation.PresentationWordDetailResponse
import com.team.prezel.core.network.model.requireData
import com.team.prezel.core.network.model.requireSuccess
import com.team.prezel.core.network.service.PresentationService
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File
import javax.inject.Inject

internal class PresentationRemoteDataSourceImpl @Inject constructor(
    private val presentationService: PresentationService,
) : PresentationRemoteDataSource {
    override suspend fun analyzePresentation(request: PresentationAnalysisRequestParts): PresentationSummaryResponse =
        presentationService
            .analyzePresentation(request.toMultipart())
            .requireData()

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

    private fun PresentationAnalysisRequestParts.toMultipart(): MultiPartFormDataContent =
        MultiPartFormDataContent(
            formData {
                append("name", name)
                append("date", date)
                append("type", type)
                append("purpose", purpose)
                append("style", style)
                append("audience", audience)
                script?.takeIf(String::isNotBlank)?.let { append("script", it) }
                appendAudioPart(audioFilePath)
            },
        )

    private fun String.toAudioMultipart(): MultiPartFormDataContent =
        MultiPartFormDataContent(
            formData {
                appendAudioPart(this@toAudioMultipart)
            },
        )

    private fun io.ktor.client.request.forms.FormBuilder.appendAudioPart(audioFilePath: String) {
        val audioFile = File(audioFilePath)

        append(
            key = "audio",
            value = audioFile.readBytes(),
            headers = Headers.build {
                append(HttpHeaders.ContentType, "audio/${audioFile.extension}")
                append(HttpHeaders.ContentDisposition, "filename=\"${audioFile.name}\"")
            },
        )
    }
}
