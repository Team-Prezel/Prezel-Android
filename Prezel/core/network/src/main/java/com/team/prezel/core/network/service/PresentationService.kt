package com.team.prezel.core.network.service

import com.team.prezel.core.network.model.BaseResponse
import com.team.prezel.core.network.model.presentation.PresentationScriptDetailResponse
import com.team.prezel.core.network.model.presentation.PresentationSummaryResponse
import com.team.prezel.core.network.model.presentation.PresentationWordDetailResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.request.forms.MultiPartFormDataContent

interface PresentationService {
    @POST("recording/analyze")
    suspend fun analyzePresentation(
        @Body multipart: MultiPartFormDataContent,
    ): BaseResponse<PresentationSummaryResponse>

    @POST("recording/{presentationId}/re-analyze")
    suspend fun reAnalyzePresentation(
        @Path("presentationId") presentationId: Long,
        @Body multipart: MultiPartFormDataContent,
    ): BaseResponse<PresentationSummaryResponse>

    @GET("recording/analyze/{analysisResultId}/scripts")
    suspend fun getScriptDetail(
        @Path("analysisResultId") analysisResultId: Long,
    ): BaseResponse<PresentationScriptDetailResponse>

    @GET("recording/analyze/{analysisResultId}/words")
    suspend fun getWordDetail(
        @Path("analysisResultId") analysisResultId: Long,
    ): BaseResponse<PresentationWordDetailResponse>

    @DELETE("recording/analyze/{analysisResultId}")
    suspend fun deleteAnalysis(
        @Path("analysisResultId") analysisResultId: Long,
    ): BaseResponse<Unit>
}
