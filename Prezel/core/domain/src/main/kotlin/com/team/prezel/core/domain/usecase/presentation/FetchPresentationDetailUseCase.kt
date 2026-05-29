package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.model.presentation.PresentationDetailWithPracticeRecords
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class FetchPresentationDetailUseCase @Inject constructor(
    private val presentationRepository: PresentationRepository,
) {
    suspend operator fun invoke(
        presentationId: Long,
        isPast: Boolean = false,
    ): Result<PresentationDetailWithPracticeRecords> =
        runCatching {
            coroutineScope {
                val presentationDetailDeferred = async {
                    if (isPast) {
                        presentationRepository.getPastPresentationDetail(presentationId = presentationId).getOrThrow()
                    } else {
                        presentationRepository.getUpcomingPresentationDetail(presentationId = presentationId).getOrThrow()
                    }
                }
                val practiceRecordsDeferred = async {
                    presentationRepository.getPracticeRecords(presentationId = presentationId).getOrThrow()
                }

                PresentationDetailWithPracticeRecords(
                    analysisSummary = presentationDetailDeferred.await(),
                    practiceRecords = practiceRecordsDeferred.await(),
                )
            }
        }
}
