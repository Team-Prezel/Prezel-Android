package com.team.prezel.core.data.repository

import com.team.prezel.core.domain.repository.terms.TermsRepository
import com.team.prezel.core.model.terms.TermsAgreement
import com.team.prezel.core.network.datasource.TermsRemoteDataSource
import com.team.prezel.core.network.model.terms.AgreeTermsRequest
import javax.inject.Inject

internal class TermsRepositoryImpl @Inject constructor(
    private val termsRemoteDataSource: TermsRemoteDataSource,
) : TermsRepository {
    override suspend fun agreeTerms(terms: List<TermsAgreement>): Result<Unit> =
        runCatching {
            termsRemoteDataSource.agreeTerms(
                request = AgreeTermsRequest(
                    terms = terms.map { term ->
                        AgreeTermsRequest.Terms(termsId = term.termsId, isAgreed = term.isAgreed)
                    },
                ),
            )
        }
}
