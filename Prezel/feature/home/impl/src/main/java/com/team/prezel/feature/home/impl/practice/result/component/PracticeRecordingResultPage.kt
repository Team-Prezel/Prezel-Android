package com.team.prezel.feature.home.impl.practice.result.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.chip.PrezelChip
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipDefaults
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipSize
import com.team.prezel.core.designsystem.component.chip.config.PrezelChipType
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.practice.PracticeRecordingSpeed
import com.team.prezel.feature.home.impl.R

private enum class PracticeAnalysisOverallResult(
    @param:StringRes val contentDescriptionResId: Int,
    @param:DrawableRes val cardResId: Int,
) {
    PERFECT(
        contentDescriptionResId = R.string.feature_home_impl_practice_recording_analysis_card_perfect,
        cardResId = R.drawable.feature_home_impl_card_perfect,
    ),
    GOOD(
        contentDescriptionResId = R.string.feature_home_impl_practice_recording_analysis_card_good,
        cardResId = R.drawable.feature_home_impl_card_good,
    ),
    TRY(
        contentDescriptionResId = R.string.feature_home_impl_practice_recording_analysis_card_try,
        cardResId = R.drawable.feature_home_impl_card_try,
    ),
}

@Composable
internal fun PracticeRecordingResultPage(
    pronunciationScore: Int,
    speed: PracticeRecordingSpeed,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val overallResult = rememberOverallResult(
        pronunciationScore = pronunciationScore,
        speed = speed,
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        PracticeRecordingResultContent(
            cardResId = overallResult.cardResId,
            cardContentDescription = stringResource(overallResult.contentDescriptionResId),
            pronunciationScore = pronunciationScore,
            speed = speed,
            modifier = Modifier.weight(1f),
        )

        PracticeRecordingResultButtonArea(onComplete = onComplete)
    }
}

@Composable
private fun PracticeRecordingResultContent(
    @DrawableRes cardResId: Int,
    cardContentDescription: String,
    pronunciationScore: Int,
    speed: PracticeRecordingSpeed,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(cardResId),
            contentDescription = cardContentDescription,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentScale = ContentScale.Fit,
        )

        PracticeAnalysisMetricRow(
            pronunciationScore = pronunciationScore,
            speed = speed,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PrezelTheme.spacing.V20, vertical = PrezelTheme.spacing.V16),
        )
    }
}

@Composable
private fun PracticeRecordingResultButtonArea(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val completeLabel = stringResource(R.string.feature_home_impl_practice_recording_analysis_complete)

    PrezelButtonArea(
        modifier = modifier,
        mainButton = { buttonModifier ->
            PrezelButton(
                text = completeLabel,
                modifier = buttonModifier,
                enabled = true,
                onClick = onComplete,
            )
        },
    )
}

private fun rememberOverallResult(
    pronunciationScore: Int,
    speed: PracticeRecordingSpeed,
): PracticeAnalysisOverallResult =
    when {
        pronunciationScore >= 95 && speed == PracticeRecordingSpeed.ADEQUATE -> PracticeAnalysisOverallResult.PERFECT
        pronunciationScore >= 70 && speed == PracticeRecordingSpeed.ADEQUATE -> PracticeAnalysisOverallResult.GOOD
        pronunciationScore >= 95 && speed != PracticeRecordingSpeed.ADEQUATE -> PracticeAnalysisOverallResult.GOOD
        pronunciationScore <= 60 -> PracticeAnalysisOverallResult.TRY
        else -> PracticeAnalysisOverallResult.TRY
    }

@Composable
private fun PracticeAnalysisMetricRow(
    pronunciationScore: Int,
    speed: PracticeRecordingSpeed,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f).padding(start = 8.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            PracticeAnalysisMetricLabel(text = stringResource(R.string.feature_home_impl_practice_recording_analysis_pronunciation))
            Text(
                text = "$pronunciationScore%",
                style = PrezelTheme.typography.body1Medium,
                color = PrezelTheme.colors.textLarge,
            )
        }

        Spacer(modifier = Modifier.width(PrezelTheme.spacing.V12))

        Box(
            modifier = Modifier
                .width(1.dp)
                .height(24.dp)
                .background(PrezelTheme.colors.borderRegular),
        )

        Spacer(modifier = Modifier.width(PrezelTheme.spacing.V12))

        Row(
            modifier = Modifier.weight(1f).padding(end = 8.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            PracticeAnalysisMetricLabel(text = stringResource(R.string.feature_home_impl_practice_recording_analysis_speed))
            PrezelChip(
                text = stringResource(speed.labelResId),
                modifier = Modifier,
                config = PrezelChipDefaults.getDefault(
                    iconOnly = false,
                    type = PrezelChipType.FILLED,
                    size = PrezelChipSize.REGULAR,
                    textStyle = PrezelTheme.typography.caption1Medium,
                    containerColor = PrezelTheme.colors.bgLarge,
                    textColor = PrezelTheme.colors.textMedium,
                ),
            )
        }
    }
}

private val PracticeRecordingSpeed.labelResId: Int
    @StringRes
    get() = when (this) {
        PracticeRecordingSpeed.SLOW -> R.string.feature_home_impl_practice_recording_analysis_speed_slow
        PracticeRecordingSpeed.ADEQUATE -> R.string.feature_home_impl_practice_recording_analysis_speed_adequate
        PracticeRecordingSpeed.FAST -> R.string.feature_home_impl_practice_recording_analysis_speed_fast
    }

@Composable
private fun PracticeAnalysisMetricLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = PrezelTheme.typography.caption1Medium,
        color = PrezelTheme.colors.textMedium,
        modifier = modifier,
    )
}

@BasicPreview
@Composable
private fun PracticeRecordingResultPerfectPagePreview() {
    PrezelTheme {
        PracticeRecordingResultPage(
            pronunciationScore = 96,
            speed = PracticeRecordingSpeed.ADEQUATE,
            onComplete = {},
        )
    }
}

@BasicPreview
@Composable
private fun PracticeRecordingResultGoodPagePreview() {
    PrezelTheme {
        PracticeRecordingResultPage(
            pronunciationScore = 90,
            speed = PracticeRecordingSpeed.ADEQUATE,
            onComplete = {},
        )
    }
}

@BasicPreview
@Composable
private fun PracticeRecordingResultTryPagePreview() {
    PrezelTheme {
        PracticeRecordingResultPage(
            pronunciationScore = 58,
            speed = PracticeRecordingSpeed.FAST,
            onComplete = {},
        )
    }
}
