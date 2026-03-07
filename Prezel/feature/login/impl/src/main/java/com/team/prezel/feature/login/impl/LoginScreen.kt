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
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.button.PrezelButton
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.login.api.AUTH_LOGO_SHARED_ELEMENT_KEY
import com.team.prezel.feature.login.impl.viewModel.LoginUiEffect
import com.team.prezel.feature.login.impl.viewModel.LoginUiState
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is LoginUiEffect.NavigateToHome -> navigateToHome()
            }
        }
    }

    LoginScreen(
        uiState = uiState,
        animatedVisibilityScope = animatedVisibilityScope,
        onLogin = viewModel::login,
        modifier = modifier,
    )
}

@Composable
private fun SharedTransitionScope.LoginScreen(
    uiState: LoginUiState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        LogoImage(
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = Modifier.align(Alignment.Center),
        )

        LoginFooter(onLogin = onLogin)
    }
}

@Composable
private fun SharedTransitionScope.LogoImage(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Image(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 95.dp)
            .padding(bottom = 52.dp)
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
private fun BoxScope.LoginFooter(
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isButtonVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isButtonVisible = true
    }

    AnimatedVisibility(
        visible = isButtonVisible,
        modifier = modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(PrezelTheme.spacing.V20),
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = AUTH_SHARED_ELEMENT_TRANSITION_DURATION,
                easing = EaseOut,
                delayMillis = AUTH_SHARED_ELEMENT_TRANSITION_DELAY,
            ),
        ),
    ) {
        // todo: 카카오 로그인으로 수정 필요
        PrezelButton(
            modifier = Modifier.fillMaxWidth(),
            text = "시작하기",
            onClick = onLogin,
        )
    }
}

@ThemePreview
@Composable
private fun LoginScreenPreview() {
    PrezelTheme {
        SharedTransitionLayout {
            AnimatedVisibility(true) {
                LoginScreen(
                    uiState = LoginUiState.Loading,
                    animatedVisibilityScope = this,
                    onLogin = {},
                )
            }
        }
    }
}
