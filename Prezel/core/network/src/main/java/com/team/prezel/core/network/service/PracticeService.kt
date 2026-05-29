package com.team.prezel.core.network.service

import com.team.prezel.core.network.model.BaseResponse
import com.team.prezel.core.network.model.practice.AnalyzePracticeRecordingResponse
import com.team.prezel.core.network.model.practice.PracticeSentenceResponse
import com.team.prezel.core.network.model.practice.PresentationRecordingAnalysisResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.request.forms.MultiPartFormDataContent

interface PracticeService {
    @GET("recording/practice/sentence")
    suspend fun getPracticeSentence(): BaseResponse<PracticeSentenceResponse>

    @POST("recording/practice/analyze")
    suspend fun analyzePracticeRecording(
        @Query("presentationId") presentationId: Long,
        @Query("referenceText") referenceText: String,
        @Body audio: MultiPartFormDataContent,
    ): BaseResponse<AnalyzePracticeRecordingResponse>

    @POST("recording/analyze")
    suspend fun analyzePresentationRecording(
        @Body multipart: MultiPartFormDataContent,
    ): BaseResponse<PresentationRecordingAnalysisResponse>
}
