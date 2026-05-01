package com.team.prezel.core.network.service

import com.team.prezel.core.network.model.BaseResponse
import com.team.prezel.core.network.model.terms.AgreeTermsRequest
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST

interface TermsService {
    @POST("terms/agree")
    suspend fun agreeTerms(
        @Body request: AgreeTermsRequest,
    ): BaseResponse<Unit>
}
