package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.practice.AnalyzePracticeRecordingResponse
import com.team.prezel.core.network.model.practice.PracticeSentenceResponse
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
}
