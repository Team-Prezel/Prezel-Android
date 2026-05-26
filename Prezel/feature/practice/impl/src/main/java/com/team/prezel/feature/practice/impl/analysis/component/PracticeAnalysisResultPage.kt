package com.team.prezel.feature.practice.impl.analysis.component

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
import com.team.prezel.core.designsystem.component.chip.chip.PrezelChip
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.practice.PracticeRecordingOverallEvaluation
import com.team.prezel.core.model.practice.RecordingSpeed
import com.team.prezel.feature.practice.impl.R

@Composable
internal fun PracticeAnalysisResultPage(
    pronunciationScore: Int,
    speed: RecordingSpeed,
    overallEvaluation: PracticeRecordingOverallEvaluation,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        PracticeRecordingResultContent(
            cardResId = overallEvaluation.cardResId,
            cardContentDescription = stringResource(overallEvaluation.contentDescriptionResId),
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
    speed: RecordingSpeed,
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
    val completeLabel = stringResource(R.string.feature_practice_impl_practice_recording_analysis_complete)

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

@Composable
private fun PracticeAnalysisMetricRow(
    pronunciationScore: Int,
    speed: RecordingSpeed,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            PracticeAnalysisMetricLabel(text = stringResource(R.string.feature_practice_impl_practice_recording_analysis_pronunciation))
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
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            PracticeAnalysisMetricLabel(text = stringResource(R.string.feature_practice_impl_practice_recording_analysis_speed))
            PrezelChip(text = stringResource(speed.labelResId))
        }
    }
}

private val RecordingSpeed.labelResId: Int
    @StringRes
    get() = when (this) {
        RecordingSpeed.SLOW -> R.string.feature_practice_impl_practice_recording_analysis_speed_slow
        RecordingSpeed.ADEQUATE -> R.string.feature_practice_impl_practice_recording_analysis_speed_adequate
        RecordingSpeed.FAST -> R.string.feature_practice_impl_practice_recording_analysis_speed_fast
    }

private val PracticeRecordingOverallEvaluation.contentDescriptionResId: Int
    @StringRes
    get() = when (this) {
        PracticeRecordingOverallEvaluation.PERFECT -> R.string.feature_practice_impl_practice_recording_analysis_card_perfect
        PracticeRecordingOverallEvaluation.GOOD -> R.string.feature_practice_impl_practice_recording_analysis_card_good
        PracticeRecordingOverallEvaluation.TRY -> R.string.feature_practice_impl_practice_recording_analysis_card_try
    }

private val PracticeRecordingOverallEvaluation.cardResId: Int
    @DrawableRes
    get() = when (this) {
        PracticeRecordingOverallEvaluation.PERFECT -> R.drawable.feature_practice_impl_card_perfect
        PracticeRecordingOverallEvaluation.GOOD -> R.drawable.feature_practice_impl_card_good
        PracticeRecordingOverallEvaluation.TRY -> R.drawable.feature_practice_impl_card_try
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
private fun PracticeAnalysisResultPerfectPagePreview() {
    PrezelTheme {
        PracticeAnalysisResultPage(
            pronunciationScore = 96,
            speed = RecordingSpeed.ADEQUATE,
            overallEvaluation = PracticeRecordingOverallEvaluation.PERFECT,
            onComplete = {},
        )
    }
}

@BasicPreview
@Composable
private fun PracticeAnalysisResultGoodPagePreview() {
    PrezelTheme {
        PracticeAnalysisResultPage(
            pronunciationScore = 90,
            speed = RecordingSpeed.ADEQUATE,
            overallEvaluation = PracticeRecordingOverallEvaluation.GOOD,
            onComplete = {},
        )
    }
}

@BasicPreview
@Composable
private fun PracticeAnalysisResultTryPagePreview() {
    PrezelTheme {
        PracticeAnalysisResultPage(
            pronunciationScore = 58,
            speed = RecordingSpeed.FAST,
            overallEvaluation = PracticeRecordingOverallEvaluation.TRY,
            onComplete = {},
        )
    }
}
