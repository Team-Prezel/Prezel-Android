package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.terms.AgreeTermsRequest
import com.team.prezel.core.network.model.terms.FetchTermsResponse

interface TermsRemoteDataSource {
    suspend fun fetchTerms(): List<FetchTermsResponse>

    suspend fun agreeTerms(request: List<AgreeTermsRequest>)
}
