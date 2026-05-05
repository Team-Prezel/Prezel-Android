package com.team.prezel.feature.splash.impl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.login.api.AUTH_LOGO_SHARED_ELEMENT_KEY
import com.team.prezel.feature.splash.impl.contract.SplashUiEffect
import com.team.prezel.feature.splash.impl.contract.SplashUiIntent
import com.team.prezel.core.designsystem.R as DSR

@Composable
internal fun SharedTransitionScope.SplashScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigateToHome: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToTerms: () -> Unit,
    navigateToCreateProfile: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val resources = LocalResources.current
    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(SplashUiIntent.CheckLoginStatus)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                SplashUiEffect.NavigateToHome -> navigateToHome()
                SplashUiEffect.NavigateToLogin -> navigateToLogin()
                SplashUiEffect.NavigateToTerms -> navigateToTerms()
                SplashUiEffect.NavigateToCreateProfile -> navigateToCreateProfile()
                SplashUiEffect.ShowRetryableFailureMessage -> {
                    snackbarHostState.showPrezelSnackbar(
                        resources.getString(R.string.feature_splash_impl_retryable_failure),
                    )
                }
            }
        }
    }

    SplashScreen(
        animatedVisibilityScope = animatedVisibilityScope,
        modifier = modifier,
    )
}

@Composable
private fun SharedTransitionScope.SplashScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(0.4722f)
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = AUTH_LOGO_SHARED_ELEMENT_KEY),
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
            painter = painterResource(DSR.drawable.core_designsystem_logo_prezel),
            contentDescription = null,
        )
    }
}

@BasicPreview
@Composable
private fun SplashScreenPreview() {
    PrezelTheme {
        SharedTransitionLayout {
            AnimatedVisibility(true) {
                SplashScreen(
                    animatedVisibilityScope = this,
                )
            }
        }
    }
}
