package com.team.prezel.core.data.repository

import com.team.prezel.core.data.error.mapDomainFailure
import com.team.prezel.core.domain.repository.terms.TermsRepository
import com.team.prezel.core.model.terms.Term
import com.team.prezel.core.model.terms.TermsAgreement
import com.team.prezel.core.network.datasource.TermsRemoteDataSource
import com.team.prezel.core.network.model.terms.AgreeTermsRequest
import com.team.prezel.core.network.model.terms.FetchTermsResponse
import javax.inject.Inject

internal class TermsRepositoryImpl @Inject constructor(
    private val termsRemoteDataSource: TermsRemoteDataSource,
) : TermsRepository {
    override suspend fun fetchTerms(): Result<List<Term>> =
        runCatching {
            termsRemoteDataSource.fetchTerms()
        }.mapCatching { response ->
            response.map { term -> term.toDomain() }
        }.mapDomainFailure()

    override suspend fun agreeTerms(terms: List<TermsAgreement>): Result<Unit> =
        runCatching {
            termsRemoteDataSource.agreeTerms(
                request = terms.map { term ->
                    AgreeTermsRequest(termsId = term.termsId, isAgreed = term.isAgreed)
                },
            )
        }.mapDomainFailure()

    private fun FetchTermsResponse.toDomain(): Term =
        Term(
            termsId = termsId.toLong(),
            title = title,
            summary = summary,
            content = content,
            isRequired = isRequired,
            version = version,
        )
}
