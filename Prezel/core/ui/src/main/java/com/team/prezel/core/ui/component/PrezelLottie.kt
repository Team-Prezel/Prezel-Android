package com.team.prezel.core.ui.component

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.R

@Composable
fun PrezelLottie(
    @RawRes resId: Int,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    iterations: Int = LottieConstants.IterateForever,
    speed: Float = 1f,
    restartOnPlay: Boolean = false,
    onAnimationFrame: ((progress: Float) -> Unit)? = null,
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resId),
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        iterations = iterations,
        speed = speed,
        restartOnPlay = restartOnPlay,
    )

    val updatedCallback by rememberUpdatedState(onAnimationFrame)
    LaunchedEffect(progress) {
        updatedCallback?.invoke(progress)
    }

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier,
    )
}

@BasicPreview
@Composable
private fun PrezelLottiePreview() {
    PrezelTheme {
        PrezelLottie(
            resId = R.raw.core_ui_asset_loading,
            iterations = LottieConstants.IterateForever,
        )
    }
}
