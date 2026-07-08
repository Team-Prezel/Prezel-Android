package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.requireData
import com.team.prezel.core.network.model.requireSuccess
import com.team.prezel.core.network.model.terms.AgreeTermsRequest
import com.team.prezel.core.network.model.terms.FetchTermsResponse
import com.team.prezel.core.network.service.TermsService
import javax.inject.Inject

internal class TermsRemoteDataSourceImpl @Inject constructor(
    private val termsService: TermsService,
) : TermsRemoteDataSource {
    override suspend fun fetchTerms(): List<FetchTermsResponse> = termsService.fetchTerms().requireData()

    override suspend fun agreeTerms(request: List<AgreeTermsRequest>) {
        termsService.agreeTerms(request = request).requireSuccess()
    }
}
