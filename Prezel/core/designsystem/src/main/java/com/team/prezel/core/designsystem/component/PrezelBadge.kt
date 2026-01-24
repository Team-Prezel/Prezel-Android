package com.team.prezel.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelBadge(
    modifier: Modifier = Modifier,
    text: String? = null,
    size: PrezelBadgeSize = PrezelBadgeSize.REGULAR,
    active: Boolean = false,
    disabled: Boolean = false,
) {
    val (backgroundColor, textColor) = badgeColors(
        disabled = disabled,
        active = active,
    )

    val badgeSize = when (size) {
        PrezelBadgeSize.SMALL -> 8.dp
        PrezelBadgeSize.REGULAR -> 20.dp
    }

    when (size) {
        PrezelBadgeSize.SMALL -> PrezelDotBadge(modifier = modifier, badgeSize = badgeSize, backgroundColor = backgroundColor)

        PrezelBadgeSize.REGULAR -> PrezelNumberBadge(
            modifier = modifier,
            badgeSize = badgeSize,
            text = text,
            backgroundColor = backgroundColor,
            textColor = textColor,
        )
    }
}

enum class PrezelBadgeSize {
    SMALL,
    REGULAR,
}

@Composable
private fun PrezelDotBadge(
    modifier: Modifier = Modifier,
    badgeSize: Dp,
    backgroundColor: Color,
) {
    Box(
        modifier = modifier
            .size(badgeSize)
            .background(
                color = backgroundColor,
                shape = PrezelTheme.shapes.V1000,
            ),
    )
}

@Composable
private fun PrezelNumberBadge(
    modifier: Modifier = Modifier,
    badgeSize: Dp,
    text: String?,
    backgroundColor: Color,
    textColor: Color,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(badgeSize)
            .background(
                color = backgroundColor,
                shape = PrezelTheme.shapes.V1000,
            ).padding(horizontal = 4.dp, vertical = 2.dp),
    ) {
        if (!text.isNullOrEmpty()) {
            Text(
                text = text,
                color = textColor,
                style = PrezelTextStyles.Caption2Regular.toTextStyle(),
            )
        }
    }
}

@Composable
private fun badgeColors(
    disabled: Boolean,
    active: Boolean,
): Pair<Color, Color> =
    when {
        disabled -> PrezelTheme.colors.bgDisabled to PrezelTheme.colors.textDisabled
        active -> PrezelTheme.colors.interactiveRegular to PrezelTheme.colors.textLarge
        else -> PrezelTheme.colors.solidBlack to PrezelTheme.colors.textLarge
    }

@ThemePreview
@Composable
private fun PrezelBadgePreview() {
    PrezelTheme {
        Column(
            modifier = Modifier
                .background(PrezelTheme.colors.bgRegular)
                .padding(16.dp),
        ) {
            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                PrezelBadge(
                    size = PrezelBadgeSize.SMALL,
                    active = false,
                    disabled = false,
                )
            }

            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                PrezelBadge(
                    size = PrezelBadgeSize.SMALL,
                    active = true,
                    disabled = false,
                )
            }

            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                PrezelBadge(
                    size = PrezelBadgeSize.SMALL,
                    active = false,
                    disabled = true,
                )
            }

            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                PrezelBadge(
                    size = PrezelBadgeSize.REGULAR,
                    text = "0",
                    active = false,
                    disabled = false,
                )
            }

            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                PrezelBadge(
                    size = PrezelBadgeSize.REGULAR,
                    text = "1",
                    active = true,
                    disabled = false,
                )
            }

            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                PrezelBadge(
                    size = PrezelBadgeSize.REGULAR,
                    text = "9",
                    active = false,
                    disabled = true,
                )
            }
        }
    }
}
