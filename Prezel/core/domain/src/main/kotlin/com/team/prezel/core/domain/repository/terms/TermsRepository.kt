package com.team.prezel.core.domain.repository.terms

import com.team.prezel.core.model.terms.Term
import com.team.prezel.core.model.terms.TermsAgreement

interface TermsRepository {
    suspend fun fetchTerms(): Result<List<Term>>

    suspend fun agreeTerms(terms: List<TermsAgreement>): Result<Unit>
}
