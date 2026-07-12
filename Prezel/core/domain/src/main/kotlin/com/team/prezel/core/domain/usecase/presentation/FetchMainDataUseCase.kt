package com.team.prezel.core.domain.usecase.presentation

import com.team.prezel.core.domain.repository.presentation.PresentationRepository
import com.team.prezel.core.domain.repository.profile.UserRepository
import com.team.prezel.core.model.presentation.Curation
import com.team.prezel.core.model.presentation.MainData
import com.team.prezel.core.model.presentation.MainDataBundle
import com.team.prezel.core.model.presentation.MainDataWithPracticeRecords
import com.team.prezel.core.model.presentation.PracticeRecords
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class FetchMainDataUseCase @Inject constructor(
    private val repository: PresentationRepository,
    private val userRepository: UserRepository,
    private val fetchCurationUseCase: FetchCurationUseCase,
) {
    suspend operator fun invoke(): Result<MainDataBundle> =
        repository.getMainData().fold(
            onSuccess = { mainData ->
                runCatching {
                    coroutineScope {
                        val nicknameDeferred = async { userRepository.getUserNickname().getOrThrow() }
                        val presentations = mainData
                            .map { data ->
                                async {
                                    val practiceRecords = repository
                                        .getPracticeRecords(presentationId = data.presentationId)
                                        .getOrThrow()
                                    val curations = fetchCurationUseCase(data.presentationId).getOrDefault(emptyList())
                                    data.toMainDataWithPracticeRecords(practiceRecords = practiceRecords, curations = curations)
                                }
                            }.awaitAll()

                        MainDataBundle(
                            nickname = nicknameDeferred.await(),
                            presentations = presentations,
                        )
                    }
                }
            },
            onFailure = { throwable -> Result.failure(throwable) },
        )

    private fun MainData.toMainDataWithPracticeRecords(
        practiceRecords: PracticeRecords,
        curations: List<Curation>,
    ): MainDataWithPracticeRecords =
        MainDataWithPracticeRecords(
            presentationId = presentationId,
            title = title,
            type = type,
            presentationDate = presentationDate,
            isPast = isPast,
            dDay = dDay,
            growthGraph = growthGraph,
            practiceRecords = practiceRecords,
            curations = curations,
        )
}
