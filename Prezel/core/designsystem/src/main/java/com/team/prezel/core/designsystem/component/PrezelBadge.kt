package com.team.prezel.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelColorScheme
import com.team.prezel.core.designsystem.theme.PrezelTheme

enum class PrezelBadgeSize {
    SMALL,
    REGULAR,
}

@Composable
fun PrezelBadge(
    active: Boolean,
    count: Int,
    modifier: Modifier = Modifier,
    size: PrezelBadgeSize = PrezelBadgeSize.REGULAR,
    disabled: Boolean = false,
) {
    val (backgroundColor, textColor) = prezelBadgeColors(
        disabled = disabled,
        active = active,
    )

    when (size) {
        PrezelBadgeSize.SMALL -> PrezelDotBadge(modifier = modifier, backgroundColor = backgroundColor)

        PrezelBadgeSize.REGULAR -> PrezelNumberBadge(
            modifier = modifier,
            count = count,
            backgroundColor = backgroundColor,
            textColor = textColor,
        )
    }
}

@Composable
private fun PrezelDotBadge(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(8.dp)
            .background(
                color = backgroundColor,
                shape = PrezelTheme.shapes.V1000,
            ),
    )
}

@Composable
private fun PrezelNumberBadge(
    count: Int,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .defaultMinSize(20.dp)
            .background(
                color = backgroundColor,
                shape = PrezelTheme.shapes.V1000,
            ).padding(horizontal = PrezelTheme.spacing.V4, vertical = PrezelTheme.spacing.V2),
    ) {
        Text(
            text = count.toString(),
            color = textColor,
            style = PrezelTextStyles.Caption2Regular.toTextStyle(),
        )
    }
}

@Composable
private fun prezelBadgeColors(
    disabled: Boolean,
    active: Boolean,
): Pair<Color, Color> =
    when {
        disabled -> PrezelTheme.colors.bgLarge to PrezelTheme.colors.textDisabled
        active -> PrezelTheme.colors.interactiveRegular to PrezelColorScheme.Dark.textLarge
        else -> PrezelTheme.colors.solidBlack to PrezelColorScheme.Dark.textLarge
    }

@ThemePreview
@Composable
private fun PrezelSmallBadgePreview() {
    PrezelTheme {
        Column(
            modifier = Modifier
                .background(PrezelTheme.colors.bgRegular)
                .padding(16.dp),
        ) {
            BadgeRowPreview(title = "Small Badge Active") {
                PrezelBadge(active = false, count = 0, size = PrezelBadgeSize.SMALL, disabled = false)
                PrezelBadge(active = true, count = 0, size = PrezelBadgeSize.SMALL, disabled = false)
            }

            BadgeRowPreview(title = "Small Badge Disabled") {
                PrezelBadge(active = false, count = 0, size = PrezelBadgeSize.SMALL, disabled = false)
                PrezelBadge(active = false, count = 0, size = PrezelBadgeSize.SMALL, disabled = true)
            }
        }
    }
}

@ThemePreview
@Composable
private fun PrezelRegularBadgePreview() {
    PrezelTheme {
        Column(
            modifier = Modifier
                .background(PrezelTheme.colors.bgRegular)
                .padding(16.dp),
        ) {
            BadgeRowPreview(title = "Regular Badge Active") {
                PrezelBadge(count = 0, active = false, disabled = false)
                PrezelBadge(count = 1, active = true, disabled = false)
            }

            BadgeRowPreview(title = "Regular Badge Disabled") {
                PrezelBadge(count = 10, active = false, disabled = false)
                PrezelBadge(count = 999, active = false, disabled = true)
            }
        }
    }
}

@Composable
private fun BadgeRowPreview(
    title: String,
    content: @Composable RowScope.() -> Unit,
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = title,
            style = PrezelTextStyles.Caption2Regular.toTextStyle(),
            color = PrezelTheme.colors.textLarge,
            modifier = Modifier.padding(bottom = 4.dp),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = content,
        )
    }
}
