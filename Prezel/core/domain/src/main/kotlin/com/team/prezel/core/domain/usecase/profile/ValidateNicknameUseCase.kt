package com.team.prezel.core.domain.usecase.profile

import com.team.prezel.core.domain.repository.profile.UserRepository
import com.team.prezel.core.model.profile.Nickname
import javax.inject.Inject

/**
 * 닉네임 유효성 및 중복 여부를 검증하는 UseCase.
 *
 * ### 동작 흐름
 * 1. 입력된 문자열을 기반으로 [Nickname.create]를 호출하여 도메인 규칙에 맞는 닉네임인지 검증합니다.
 * 2. 닉네임 생성에 성공한 경우, [UserRepository.checkNicknameDuplication]을 통해 서버에 중복 여부를 확인합니다.
 * 3. 각 단계의 결과에 따라 [Result]를 반환합니다.
 *
 */
class ValidateNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(nickname: String): Result =
        when (val creationResult = Nickname.create(nickname)) {
            is Nickname.CreationResult.Success -> {
                userRepository.checkNicknameDuplication(creationResult.nickname).fold(
                    onSuccess = { isDuplicated ->
                        if (isDuplicated) Result.Invalid.Duplicated else Result.Available(creationResult.nickname)
                    },
                    onFailure = { throwable ->
                        Result.Error(throwable)
                    },
                )
            }

            is Nickname.CreationResult.Failure -> Result.Invalid.Format(reason = creationResult.reason)
        }

    sealed interface Result {
        data class Available(
            val nickname: Nickname,
        ) : Result

        sealed interface Invalid : Result {
            data class Format(
                val reason: Nickname.InvalidReason,
            ) : Invalid

            data object Duplicated : Invalid
        }

        data class Error(
            val throwable: Throwable,
        ) : Result
    }
}
