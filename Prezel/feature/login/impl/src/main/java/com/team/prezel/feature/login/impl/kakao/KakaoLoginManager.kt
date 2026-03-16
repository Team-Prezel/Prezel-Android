package com.team.prezel.feature.login.impl.kakao

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class KakaoLoginManager {
    suspend fun login(context: Context): KakaoLoginResult =
        suspendCancellableCoroutine { continuation ->
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                when {
                    error != null -> continuation.resume(KakaoLoginResult.Failure(error))
                    token != null -> continuation.resume(KakaoLoginResult.Success(token.accessToken))
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

    suspend fun logout(): Result<Unit> =
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
