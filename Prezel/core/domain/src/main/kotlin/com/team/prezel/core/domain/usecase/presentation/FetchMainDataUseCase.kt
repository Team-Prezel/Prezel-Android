package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.model.presentation.MainData
import javax.inject.Inject

class FetchMainDataUseCase @Inject constructor(
    private val repository: PresentationRepository,
) {
    suspend operator fun invoke(): Result<List<MainData>> = repository.getMainData()
}
