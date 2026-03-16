package com.team.prezel.feature.login.impl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.button.ButtonAreaButtonSpec
import com.team.prezel.core.designsystem.component.button.PrezelButtonArea
import com.team.prezel.core.designsystem.component.snackbar.PrezelSnackbar
import com.team.prezel.core.designsystem.component.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.designsystem.util.clickOnce
import com.team.prezel.feature.login.api.AUTH_LOGO_SHARED_ELEMENT_KEY
import com.team.prezel.feature.login.impl.kakao.KakaoLoginManager
import com.team.prezel.feature.login.impl.kakao.KakaoLoginResult
import com.team.prezel.feature.login.impl.viewModel.LoginUiEffect
import com.team.prezel.feature.login.impl.viewModel.LoginUiIntent
import com.team.prezel.feature.login.impl.viewModel.LoginViewModel
import com.team.prezel.core.designsystem.R as DSR

private const val AUTH_SHARED_ELEMENT_TRANSITION_DURATION = 300
private const val AUTH_SHARED_ELEMENT_TRANSITION_DELAY = 400

@Composable
internal fun SharedTransitionScope.LoginScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val kakaoLoginManager = remember { KakaoLoginManager() }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                LoginUiEffect.LaunchKakaoLogin -> {
                    when (kakaoLoginManager.login(context)) {
                        is KakaoLoginResult.Success -> {
                            viewModel.onIntent(LoginUiIntent.LoginSucceeded)
                        }

                        is KakaoLoginResult.Failure -> {
                            viewModel.onIntent(
                                LoginUiIntent.LoginFailed(
                                    message = "카카오 로그인에 실패했습니다. 다시 시도해주세요.",
                                ),
                            )
                        }
                    }
                }

                LoginUiEffect.NavigateToHome -> navigateToHome()

                is LoginUiEffect.ShowSnackbar -> {
                    snackbarHostState.showPrezelSnackbar(
                        message = effect.message,
                    )
                }
            }
        }
    }

    LoginScreen(
        animatedVisibilityScope = animatedVisibilityScope,
        isLoading = uiState.isLoading,
        onLogin = { viewModel.onIntent(LoginUiIntent.OnClickLogin) },
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}

@Composable
private fun SharedTransitionScope.LoginScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    isLoading: Boolean,
    onLogin: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
        ) {
            LogoImage(
                animatedVisibilityScope = animatedVisibilityScope,
                modifier = Modifier.weight(1f),
            )

            LoginFooter(isLoading = isLoading, onLogin = onLogin)
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
            snackbar = { data ->
                PrezelSnackbar(data = data)
            },
        )
    }
}

@Composable
private fun SharedTransitionScope.LogoImage(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Image(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 95.dp)
            .sharedElement(
                sharedContentState = rememberSharedContentState(key = AUTH_LOGO_SHARED_ELEMENT_KEY),
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = { _, _ ->
                    tween(
                        durationMillis = AUTH_SHARED_ELEMENT_TRANSITION_DURATION,
                        easing = EaseOut,
                        delayMillis = AUTH_SHARED_ELEMENT_TRANSITION_DELAY,
                    )
                },
            ),
        painter = painterResource(DSR.drawable.core_designsystem_logo_prezel),
        contentDescription = null,
    )
}

@Composable
private fun LoginFooter(
    isLoading: Boolean,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isButtonVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isButtonVisible = true
    }

    AnimatedVisibility(
        visible = isButtonVisible,
        modifier = modifier,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = AUTH_SHARED_ELEMENT_TRANSITION_DURATION,
                easing = EaseOut,
                delayMillis = AUTH_SHARED_ELEMENT_TRANSITION_DELAY,
            ),
        ),
    ) {
        PrezelButtonArea(
            mainButton = ButtonAreaButtonSpec(
                icon = IconSource(painter = painterResource(PrezelIcons.Kakao)),
                label = "카카오로 시작하기",
                enabled = !isLoading,
                onClick = onLogin.clickOnce(),
            ),
            subButton = null,
        )
    }
}

@ThemePreview
@Composable
private fun LoginScreenPreview() {
    PrezelTheme {
        val snackbarHostState = remember { SnackbarHostState() }

        SharedTransitionLayout {
            AnimatedVisibility(true) {
                LoginScreen(
                    animatedVisibilityScope = this,
                    isLoading = false,
                    onLogin = {},
                    snackbarHostState = snackbarHostState,
                )
            }
        }
    }
}
