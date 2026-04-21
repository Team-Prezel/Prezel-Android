package com.team.prezel.feature.login.impl.landing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.team.prezel.core.auth.AuthManager
import com.team.prezel.core.auth.model.AuthProvider
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.component.modal.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.login.api.AUTH_LOGO_SHARED_ELEMENT_KEY
import com.team.prezel.feature.login.impl.R
import com.team.prezel.feature.login.impl.landing.contract.LoginUiEffect
import com.team.prezel.feature.login.impl.landing.contract.LoginUiIntent
import com.team.prezel.feature.login.impl.landing.contract.LoginUiState
import com.team.prezel.feature.login.impl.landing.model.LoginUiMessage
import com.team.prezel.core.designsystem.R as DSR

private const val AUTH_SHARED_ELEMENT_TRANSITION_DURATION = 300
private const val AUTH_SHARED_ELEMENT_TRANSITION_DELAY = 400

@Composable
internal fun SharedTransitionScope.LoginScreen(
    authManager: AuthManager,
    navigateToTerms: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val snackbarHostState = LocalSnackbarHostState.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is LoginUiEffect.LaunchLogin -> {
                    authManager.login(context = context, provider = effect.provider).also { result ->
                        viewModel.onIntent(LoginUiIntent.OnLoginResult(result = result))
                    }
                }

                LoginUiEffect.NavigateToTerms -> navigateToTerms()

                LoginUiEffect.NavigateToHome -> navigateToHome()

                is LoginUiEffect.ShowMessage -> {
                    val resId = when (effect.message) {
                        LoginUiMessage.LoginCancelled -> R.string.feature_login_impl_kakao_cancelled
                        LoginUiMessage.LoginFailedRateLimited -> R.string.feature_login_impl_kakao_rate_limited
                        LoginUiMessage.LoginFailedUnknown -> R.string.feature_login_impl_kakao_failure
                    }
                    snackbarHostState.showPrezelSnackbar(message = resources.getString(resId))
                }
            }
        }
    }

    LoginScreen(
        uiState = uiState,
        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
        onLogin = { viewModel.onIntent(LoginUiIntent.OnClickLogin(provider = AuthProvider.KAKAO)) },
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LogoImage(
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = Modifier.weight(1f),
        )

        LoginFooter(
            enabled = !uiState.isLoading,
            onLogin = onLogin,
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
            .fillMaxWidth(0.4722f)
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
    enabled: Boolean,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isButtonVisible by remember { mutableStateOf(false) }
    val startWithKakaoLabel = stringResource(R.string.feature_login_impl_start_with_kakao)
    val kakaoButtonConfig = PrezelButtonDefaults.getDefault(
        isIconOnly = false,
        type = ButtonType.FILLED,
        size = ButtonSize.REGULAR,
        hierarchy = ButtonHierarchy.SECONDARY,
        isRounded = false,
        backgroundColor = Color(0xFFFEE500),
        contentColor = PrezelTheme.colors.textLarge,
    )

    LaunchedEffect(Unit) {
        isButtonVisible = true
    }

    AnimatedVisibility(
        visible = isButtonVisible,
        modifier = modifier.fillMaxWidth(),
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = AUTH_SHARED_ELEMENT_TRANSITION_DURATION,
                easing = EaseOut,
                delayMillis = AUTH_SHARED_ELEMENT_TRANSITION_DELAY,
            ),
        ),
        exit = ExitTransition.None,
    ) {
        PrezelButtonArea {
            CustomButton(
                iconResId = PrezelIcons.Kakao,
                label = startWithKakaoLabel,
                enabled = enabled,
                onClick = onLogin,
                config = kakaoButtonConfig,
            )
        }
    }
}

@BasicPreview
@Composable
private fun LoginScreenPreview() {
    PrezelTheme {
        SharedTransitionLayout {
            AnimatedVisibility(true) {
                LoginScreen(
                    uiState = LoginUiState(),
                    animatedVisibilityScope = this,
                    onLogin = {},
                )
            }
        }
    }
}
