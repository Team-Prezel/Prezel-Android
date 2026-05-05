package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.terms.AgreeTermsRequest

interface TermsRemoteDataSource {
    suspend fun agreeTerms(request: List<AgreeTermsRequest>)
}
