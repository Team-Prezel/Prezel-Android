package com.team.prezel.core.auth

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class KakaoLoginManagerImpl
    @Inject
    constructor() : KakaoLoginManager {
        override suspend fun login(context: Context): KakaoLoginResult =
            suspendCancellableCoroutine { continuation ->
                val accountLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                    when {
                        error != null -> {
                            Timber.e(error, "카카오 계정 로그인에 실패했습니다.")
                            continuation.resume(error.toLoginResult())
                        }

                        token != null -> {
                            Timber.d("카카오 계정 로그인에 성공했습니다.")
                            continuation.resume(KakaoLoginResult.Success)
                        }

                        else -> continuation.resume(
                            KakaoLoginResult.Failure(
                                IllegalStateException("카카오 로그인 결과가 비어있습니다."),
                            ),
                        )
                    }
                }

                if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                    UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                        when {
                            error is ClientError && error.reason == ClientErrorCause.Cancelled -> {
                                Timber.w(error, "사용자가 카카오톡 로그인을 취소했습니다.")
                                continuation.resume(KakaoLoginResult.Failure(error))
                            }

                            error != null -> {
                                Timber.w(error, "카카오톡 로그인에 실패해 카카오 계정 로그인으로 전환합니다.")
                                UserApiClient.instance.loginWithKakaoAccount(
                                    context,
                                    callback = accountLoginCallback,
                                )
                            }

                            token != null -> {
                                Timber.d("카카오톡 로그인에 성공했습니다.")
                                continuation.resume(KakaoLoginResult.Success)
                            }

                            else -> continuation.resume(
                                KakaoLoginResult.Failure(
                                    IllegalStateException("카카오톡 로그인 결과가 비어있습니다."),
                                ),
                            )
                        }
                    }
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = accountLoginCallback)
                }
            }

        private fun Throwable.toLoginResult(): KakaoLoginResult =
            if (this is AuthError && statusCode == 429) {
                KakaoLoginResult.RateLimited(this)
            } else {
                KakaoLoginResult.Failure(this)
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
