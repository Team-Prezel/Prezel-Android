package com.team.prezel.core.network.service

import com.team.prezel.core.network.model.BaseResponse
import com.team.prezel.core.network.model.terms.AgreeTermsRequest
import com.team.prezel.core.network.model.terms.FetchTermsResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST

interface TermsService {
    @POST("terms/agree")
    suspend fun agreeTerms(
        @Body request: List<AgreeTermsRequest>,
    ): BaseResponse<Unit>

    @GET("terms")
    suspend fun fetchTerms(): BaseResponse<List<FetchTermsResponse>>
}
