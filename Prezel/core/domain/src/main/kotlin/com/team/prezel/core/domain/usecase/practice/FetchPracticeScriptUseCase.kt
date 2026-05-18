package com.team.prezel.core.domain.usecase.practice

import com.team.prezel.core.domain.repository.practice.PracticeRepository
import com.team.prezel.core.model.practice.PracticeScript
import javax.inject.Inject

class FetchPracticeScriptUseCase @Inject constructor(
    private val practiceRepository: PracticeRepository,
) {
    suspend operator fun invoke(): Result<PracticeScript> = practiceRepository.fetchPracticeScript()
}
