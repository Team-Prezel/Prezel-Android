package com.team.prezel.feature.report.impl.report.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.team.prezel.core.designsystem.component.actions.button.PrezelIconButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlin.math.roundToInt

@Composable
internal fun MetricResultCard(
    title: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
) {
    PrezelTouchArea(
        modifier = modifier
            .clip(shape = PrezelTheme.shapes.V8)
            .background(color = PrezelTheme.colors.bgMedium),
        enabled = enabled,
        shape = PrezelTheme.shapes.V8,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = PrezelTheme.spacing.V12,
                    bottom = PrezelTheme.spacing.V12,
                    top = PrezelTheme.spacing.V14,
                ),
        ) {
            Text(
                text = title,
                style = PrezelTheme.typography.body3Regular,
                color = PrezelTheme.colors.textRegular,
            )

            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V2))

            Text(
                text = value,
                style = PrezelTheme.typography.title1Bold,
                color = PrezelTheme.colors.textMedium,
            )
        }
        PrezelIconButton(
            iconResId = PrezelIcons.ArrowTopRight,
            type = ButtonType.GHOST,
            size = ButtonSize.SMALL,
            hierarchy = ButtonHierarchy.SECONDARY,
            modifier = Modifier.align(Alignment.TopEnd),
            enabled = enabled,
            isUseRipple = false,
            onClick = onClick,
        )
    }
}

internal fun Double?.toPercentLabel(): String {
    val base = this?.roundToInt()
    return "${base ?: "-"}%"
}

@BasicPreview
@Composable
private fun MetricResultCardPreview() {
    PrezelTheme {
        MetricResultCard(
            title = "발화 정확도",
            value = "82%",
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
        )
    }
}
