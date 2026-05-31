package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.model.presentation.PresentationDetailWithPracticeRecords
import javax.inject.Inject

class FetchPresentationDetailUseCase @Inject constructor(
    private val presentationRepository: PresentationRepository,
) {
    suspend operator fun invoke(
        presentationId: Long,
        isPast: Boolean = false,
    ): Result<PresentationDetailWithPracticeRecords> =
        if (isPast) {
            getPastPresentationDetail(presentationId = presentationId)
        } else {
            getUpcomingPresentationDetail(presentationId = presentationId)
        }

    private suspend fun getPastPresentationDetail(presentationId: Long): Result<PresentationDetailWithPracticeRecords> =
        presentationRepository
            .getPastPresentationDetail(presentationId = presentationId)
            .mapCatching { detail ->
                val practiceRecords = presentationRepository.getPracticeRecords(presentationId = presentationId).getOrThrow()
                PresentationDetailWithPracticeRecords(
                    analysisSummary = detail,
                    practiceRecords = practiceRecords,
                )
            }

    private suspend fun getUpcomingPresentationDetail(presentationId: Long): Result<PresentationDetailWithPracticeRecords> =
        presentationRepository
            .getPastPresentationDetail(presentationId = presentationId)
            .mapCatching { detail ->
                PresentationDetailWithPracticeRecords(analysisSummary = detail, practiceRecords = null)
            }
}
