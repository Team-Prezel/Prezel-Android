package com.team.prezel.core.data.auth

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class KakaoLoginManagerImpl @Inject constructor() : KakaoLoginManager {
    override suspend fun login(context: Context): KakaoLoginResult =
        suspendCancellableCoroutine { continuation ->
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                when {
                    error != null -> continuation.resume(KakaoLoginResult.Failure(error))
                    token != null -> continuation.resume(KakaoLoginResult.Success)
                    else -> continuation.resume(
                        KakaoLoginResult.Failure(
                            IllegalStateException("카카오 로그인 결과가 비어있습니다."),
                        ),
                    )
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
            }
        }

    override suspend fun logout(): Result<Unit> =
        suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    continuation.resume(Result.failure(error))
                } else {
                    continuation.resume(Result.success(Unit))
                }
            }
        }
}
