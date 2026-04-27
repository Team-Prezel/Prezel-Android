package com.team.prezel.core.auth

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.team.prezel.core.auth.model.AuthResult
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume

class KakaoAuthClient @Inject constructor() : AuthClient {
    override suspend fun login(context: Context): AuthResult =
        suspendCancellableCoroutine { continuation ->
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                loginWithKakaoTalk(context = context, continuation = continuation)
                return@suspendCancellableCoroutine
            }

            loginWithKakaoAccount(context = context, continuation = continuation)
        }

    override suspend fun logout(): Result<Unit> =
        suspendCancellableCoroutine { continuation ->
            Timber.d("카카오 로그아웃 시도")

            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Timber.e(error, "카카오 로그아웃에 실패했습니다.")
                    continuation.resume(Result.failure(error))
                    return@logout
                }

                Timber.d("카카오 로그아웃에 성공했습니다.")
                continuation.resume(Result.success(Unit))
            }
        }

    private fun loginWithKakaoTalk(
        context: Context,
        continuation: CancellableContinuation<AuthResult>,
    ) {
        Timber.d("카카오톡으로 로그인 시도")
        UserApiClient.instance.loginWithKakaoTalk(
            context = context,
            callback = continuation.loginCallback(loginType = "카카오톡"),
        )
    }

    private fun loginWithKakaoAccount(
        context: Context,
        continuation: CancellableContinuation<AuthResult>,
    ) {
        Timber.d("카카오 계정으로 로그인 시도")
        UserApiClient.instance.loginWithKakaoAccount(
            context = context,
            callback = continuation.loginCallback(loginType = "카카오 계정"),
        )
    }

    private fun CancellableContinuation<AuthResult>.loginCallback(loginType: String): (OAuthToken?, Throwable?) -> Unit =
        { token, error ->
            when {
                error != null -> {
                    val authResult = error.toAuthResult()
                    when (authResult) {
                        AuthResult.Cancelled -> Timber.i(error, "$loginType 로그인이 취소되었습니다.")
                        is AuthResult.Failure -> Timber.w(error, "$loginType 로그인에 실패했습니다.")
                        is AuthResult.Success -> Unit
                    }
                    resume(authResult)
                }

                token != null -> {
                    val idToken = token.idToken
                    if (idToken.isNullOrBlank()) {
                        val throwable = IllegalStateException("$loginType 로그인에 성공했지만 idToken이 비어있습니다.")
                        Timber.w(throwable)
                        resume(AuthResult.Failure(throwable))
                    } else {
                        Timber.d("$loginType 로그인에 성공했습니다.")
                        resume(AuthResult.Success(idToken = idToken))
                    }
                }

                else -> {
                    val throwable = IllegalStateException("$loginType 로그인 결과가 비어있습니다.")
                    Timber.w(throwable)
                    resume(AuthResult.Failure(throwable))
                }
            }
        }

    private fun Throwable.toAuthResult(): AuthResult {
        if (this is AuthError) return toAuthErrorResult()
        if (this is ClientError) return toClientErrorResult()
        return AuthResult.Failure(this)
    }

    private fun AuthError.toAuthErrorResult(): AuthResult {
        if (reason == AuthErrorCause.AccessDenied) return AuthResult.Cancelled
        return AuthResult.Failure(this)
    }

    private fun ClientError.toClientErrorResult(): AuthResult {
        if (reason == ClientErrorCause.Cancelled) return AuthResult.Cancelled
        return AuthResult.Failure(this)
    }
}
