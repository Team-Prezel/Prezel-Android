package com.team.prezel.core.domain.usecase.terms

import com.team.prezel.core.domain.repository.terms.TermsRepository
import com.team.prezel.core.model.terms.Term
import javax.inject.Inject

class FetchTermsUseCase @Inject constructor(
    private val termsRepository: TermsRepository,
) {
    suspend operator fun invoke(): Result<List<Term>> = termsRepository.fetchTerms()
}
