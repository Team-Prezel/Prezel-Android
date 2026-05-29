package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.practice.AnalyzePracticeRecordingResponse
import com.team.prezel.core.network.model.practice.PracticeSentenceResponse
import com.team.prezel.core.network.model.requireData
import com.team.prezel.core.network.service.PracticeService
import io.ktor.client.request.forms.ChannelProvider
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import java.io.File
import javax.inject.Inject

internal class PracticeRemoteDataSourceImpl @Inject constructor(
    private val practiceService: PracticeService,
) : PracticeRemoteDataSource {
    override suspend fun getPracticeSentence(): PracticeSentenceResponse = practiceService.getPracticeSentence().requireData()

    override suspend fun analyzePracticeRecording(
        presentationId: Long,
        recordingFilePath: String,
        referenceText: String,
    ): AnalyzePracticeRecordingResponse {
        require(recordingFilePath.isNotBlank()) { "녹음 파일 경로가 비어 있습니다." }

        val audioFile = File(recordingFilePath)
        val multipart = MultiPartFormDataContent(
            formData {
                append(
                    key = "audio",
                    value = audioFile.toChannelProvider(),
                    headers = Headers.build {
                        append(HttpHeaders.ContentType, "audio/${audioFile.extension}")
                        append(HttpHeaders.ContentDisposition, "filename=\"${audioFile.name}\"")
                    },
                )
            },
        )

        return practiceService
            .analyzePracticeRecording(
                presentationId = presentationId,
                referenceText = referenceText,
                audio = multipart,
            ).requireData()
    }

    private fun File.toChannelProvider(): ChannelProvider =
        ChannelProvider(size = length()) {
            require(exists()) { "녹음 파일이 존재하지 않습니다: $path" }
            require(canRead()) { "녹음 파일을 읽을 수 없습니다: $path" }
            inputStream().toByteReadChannel()
        }
}
