package com.team.prezel.core.network.service

import com.team.prezel.core.network.model.BaseResponse
import com.team.prezel.core.network.model.presentation.GetMainDataResponse
import com.team.prezel.core.network.model.presentation.GetPracticeRecordsResponse
import com.team.prezel.core.network.model.presentation.GetPresentationDetailResponse
import com.team.prezel.core.network.model.presentation.GetPresentationsResponse
import com.team.prezel.core.network.model.presentation.PresentationScriptDetailResponse
import com.team.prezel.core.network.model.presentation.PresentationSummaryResponse
import com.team.prezel.core.network.model.presentation.PresentationWordDetailResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
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

    @GET("recording/upcoming")
    suspend fun getUpcomingPresentations(): BaseResponse<List<GetPresentationsResponse>>

    @GET("recording/past")
    suspend fun getPastPresentations(): BaseResponse<List<GetPresentationsResponse>>

    @GET("recording/{presentationId}/upcoming")
    suspend fun getUpcomingPresentationDetail(
        @Path("presentationId") presentationId: Long,
    ): BaseResponse<GetPresentationDetailResponse>

    @GET("recording/{presentationId}/past")
    suspend fun getPastPresentationDetail(
        @Path("presentationId") presentationId: Long,
    ): BaseResponse<GetPresentationDetailResponse>

    @GET("recording/{presentationId}/practice-records")
    suspend fun getPracticeRecords(
        @Path("presentationId") presentationId: Long,
    ): BaseResponse<GetPracticeRecordsResponse>

    @GET("main")
    suspend fun getMainData(): BaseResponse<List<GetMainDataResponse>>
}
