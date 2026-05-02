package com.team.prezel.core.domain.usecase.practice

import com.team.prezel.core.domain.repository.practice.PracticeRepository
import com.team.prezel.core.model.practice.PracticeScript
import javax.inject.Inject

/**
 * 연습 녹음에 사용할 대본을 조회하는 UseCase.
 *
 * ### 동작 흐름
 * 1. [com.team.prezel.core.domain.repository.practice.PracticeRepository.fetchPracticeScript]를 호출하여 연습 대본 조회를 요청합니다.
 * 2. repository가 서버 또는 임시 데이터 소스로부터 대본을 가져옵니다.
 * 3. 조회 결과에 따라 연습 대본 또는 예외를 포함한 [Result]를 반환합니다.
 */
class FetchPracticeScriptUseCase @Inject constructor(
    private val practiceRepository: PracticeRepository,
) {
    suspend operator fun invoke(): Result<PracticeScript> = practiceRepository.fetchPracticeScript()
}
