package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.model.presentation.MainData
import com.team.prezel.core.model.presentation.MainDataWithPracticeRecords
import com.team.prezel.core.model.presentation.PracticeRecords
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class FetchMainDataUseCase @Inject constructor(
    private val repository: PresentationRepository,
) {
    suspend operator fun invoke(): Result<List<MainDataWithPracticeRecords>> =
        repository.getMainData().fold(
            onSuccess = { mainData ->
                runCatching {
                    coroutineScope {
                        mainData
                            .map { data ->
                                async {
                                    val practiceRecords = repository
                                        .getPracticeRecords(presentationId = data.presentationId)
                                        .getOrThrow()
                                    data.toMainDataWithPracticeRecords(practiceRecords = practiceRecords)
                                }
                            }.awaitAll()
                    }
                }
            },
            onFailure = { throwable -> Result.failure(throwable) },
        )

    private fun MainData.toMainDataWithPracticeRecords(practiceRecords: PracticeRecords): MainDataWithPracticeRecords =
        MainDataWithPracticeRecords(
            presentationId = presentationId,
            title = title,
            type = type,
            presentationDate = presentationDate,
            isPast = isPast,
            dDay = dDay,
            growthGraph = growthGraph,
            practiceRecords = practiceRecords,
        )
}
