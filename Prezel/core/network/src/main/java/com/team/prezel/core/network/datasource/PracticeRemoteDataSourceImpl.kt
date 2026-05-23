package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.practice.AnalyzePracticeRecordingResponse
import com.team.prezel.core.network.model.practice.PracticeSentenceResponse
import com.team.prezel.core.network.model.practice.PresentationAnalysisAudience
import com.team.prezel.core.network.model.practice.PresentationAnalysisPurpose
import com.team.prezel.core.network.model.practice.PresentationAnalysisStyle
import com.team.prezel.core.network.model.practice.PresentationAnalysisType
import com.team.prezel.core.network.model.practice.PresentationRecordingAnalysisResponse
import com.team.prezel.core.network.model.requireData
import com.team.prezel.core.network.service.PracticeService
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File
import javax.inject.Inject

internal class PracticeRemoteDataSourceImpl @Inject constructor(
    private val practiceService: PracticeService,
) : PracticeRemoteDataSource {
    override suspend fun getPracticeSentence(): PracticeSentenceResponse = practiceService.getPracticeSentence().requireData()

    override suspend fun analyzePracticeRecording(
        recordingFilePath: String,
        referenceText: String,
    ): AnalyzePracticeRecordingResponse {
        val audioFile = File(recordingFilePath)
        val multipart = MultiPartFormDataContent(
            formData {
                append(
                    key = "audio",
                    value = audioFile.readBytes(),
                    headers = Headers.build {
                        append(HttpHeaders.ContentType, "audio/${audioFile.extension}")
                        append(HttpHeaders.ContentDisposition, "filename=\"${audioFile.name}\"")
                    },
                )
            },
        )

        return practiceService
            .analyzePracticeRecording(
                referenceText = referenceText,
                audio = multipart,
            ).requireData()
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
        val audioFile = File(audioFilePath)
        val scriptFile = scriptFilePath?.let(::File)
        val multipart = MultiPartFormDataContent(
            formData {
                append("name", name)
                append("date", date)
                append("type", type.value)
                append("purpose", purpose.value)
                append("style", style.value)
                append("audience", audience.value)
                script?.takeIf(String::isNotBlank)?.let { append("script", it) }
                scriptFile?.let { file ->
                    append(
                        key = "scriptFile",
                        value = file.readBytes(),
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, "text/${file.extension}")
                            append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                        },
                    )
                }
                append(
                    key = "audio",
                    value = audioFile.readBytes(),
                    headers = Headers.build {
                        append(HttpHeaders.ContentType, "audio/${audioFile.extension}")
                        append(HttpHeaders.ContentDisposition, "filename=\"${audioFile.name}\"")
                    },
                )
            },
        )

        return practiceService
            .analyzePresentationRecording(multipart = multipart)
            .requireData()
    }
}
