package com.team.prezel.feature.analysis.impl.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.analysis.impl.R
import com.team.prezel.feature.analysis.impl.contract.AnalysisSubmitState
import kotlin.math.roundToInt

private const val UPLOAD_PROGRESS_ANIMATION_MILLIS = 700

@Composable
internal fun AnalysisSubmitButton(
    submitState: AnalysisSubmitState,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (submitState) {
        AnalysisSubmitState.Idle -> PrezelButton(
            modifier = modifier,
            text = stringResource(R.string.feature_analysis_impl_analyze),
            enabled = enabled,
            onClick = onClick,
            type = ButtonType.FILLED,
            hierarchy = ButtonHierarchy.PRIMARY,
        )

        is AnalysisSubmitState.Uploading -> {
            val displayedProgress by animateFloatAsState(
                targetValue = submitState.progress.coerceIn(0f, 1f),
                animationSpec = tween(durationMillis = UPLOAD_PROGRESS_ANIMATION_MILLIS),
                label = "analysis-upload-progress",
            )

            AnalysisSubmitStatusButton(
                modifier = modifier,
                text = "${(displayedProgress * 100).roundToInt()}%",
            )
        }

        AnalysisSubmitState.Uploaded -> AnalysisSubmitStatusButton(
            modifier = modifier,
            text = stringResource(R.string.feature_analysis_impl_upload_complete),
        )
    }
}

@Composable
private fun AnalysisSubmitStatusButton(
    text: String,
    modifier: Modifier = Modifier,
) {
    val config = PrezelButtonDefaults.getDefault(
        isIconOnly = false,
        isRounded = false,
        type = ButtonType.FILLED,
        size = ButtonSize.REGULAR,
        hierarchy = ButtonHierarchy.PRIMARY,
    )

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(PrezelTheme.radius.V8))
            .background(color = config.backgroundColor(enabled = true)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = config.contentColor(enabled = true),
            style = config.textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
}
