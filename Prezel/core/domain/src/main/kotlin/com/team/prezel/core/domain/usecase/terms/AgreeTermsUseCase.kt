package com.team.prezel.core.domain.usecase.terms

import com.team.prezel.core.domain.repository.terms.TermsRepository
import com.team.prezel.core.model.terms.TermsAgreement
import javax.inject.Inject

class AgreeTermsUseCase @Inject constructor(
    private val termsRepository: TermsRepository,
) {
    suspend operator fun invoke(terms: List<TermsAgreement>): Result<Unit> = termsRepository.agreeTerms(terms = terms)
}
