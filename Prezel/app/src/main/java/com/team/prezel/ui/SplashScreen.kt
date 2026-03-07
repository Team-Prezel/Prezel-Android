package com.team.prezel.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.team.prezel.R
import com.team.prezel.core.designsystem.component.button.PrezelButton
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

private const val SPLASH_DURATION = 300
private const val SPLASH_ANIMATION_DELAY = 400L

@Composable
fun SplashScreen(
    onSplashEnded: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val logoOffsetY = remember { Animatable(0f) }
    var showBottomButton by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(SPLASH_ANIMATION_DELAY)
        showBottomButton = true
        logoOffsetY.animateTo(
            targetValue = -with(density) { 52.dp.toPx() },
            animationSpec = tween(
                durationMillis = SPLASH_DURATION,
                easing = EaseOut,
            ),
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 95.dp)
                .offset {
                    IntOffset(
                        x = 0,
                        y = logoOffsetY.value.roundToInt(),
                    )
                },
            painter = painterResource(R.drawable.logo_prezel),
            contentDescription = stringResource(R.string.splash_logo_description),
        )

        AnimatedVisibility(
            visible = showBottomButton,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(PrezelTheme.spacing.V20),
            enter = fadeIn(animationSpec = tween(durationMillis = SPLASH_DURATION)),
        ) {
            PrezelButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.splash_start_button),
                onClick = onSplashEnded,
            )
        }
    }
}

@ThemePreview
@Composable
private fun SplashScreenPreview() {
    PrezelTheme {
        SplashScreen(onSplashEnded = {})
    }
}
