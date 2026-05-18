package com.team.prezel.core.network.service

import com.team.prezel.core.network.model.BaseResponse
import com.team.prezel.core.network.model.practice.AnalyzePracticeRecordingResponse
import com.team.prezel.core.network.model.practice.PracticeSentenceResponse
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
        @Query("referenceText") referenceText: String,
        @Body audio: MultiPartFormDataContent,
    ): BaseResponse<AnalyzePracticeRecordingResponse>
}
