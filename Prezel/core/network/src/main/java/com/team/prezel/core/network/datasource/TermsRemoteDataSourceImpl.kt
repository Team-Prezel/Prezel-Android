package com.team.prezel.core.network.datasource

import com.team.prezel.core.network.model.requireSuccess
import com.team.prezel.core.network.model.terms.AgreeTermsRequest
import com.team.prezel.core.network.service.TermsService
import javax.inject.Inject

internal class TermsRemoteDataSourceImpl @Inject constructor(
    private val termsService: TermsService,
) : TermsRemoteDataSource {
    override suspend fun agreeTerms(request: AgreeTermsRequest) {
        termsService.agreeTerms(request = request).requireSuccess()
    }
}
