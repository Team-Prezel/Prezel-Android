package com.team.prezel.core.designsystem.component.badge

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
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelBadge(
    modifier: Modifier = Modifier,
    text: String? = null,
    size: PrezelBadgeSize = PrezelBadgeSize.REGULAR,
    active: PrezelBadgeActive = PrezelBadgeActive.OFF,
    disabled: PrezelBadgeDisabled = PrezelBadgeDisabled.OFF,
) {
    val (backgroundColor, textColor) = badgeColors(
        disabled = disabled,
        active = active,
    )

    val dimension = when (size) {
        PrezelBadgeSize.SMALL -> 8.dp
        PrezelBadgeSize.REGULAR -> 20.dp
    }

    when (size) {
        PrezelBadgeSize.SMALL -> {
            Box(
                modifier = modifier
                    .size(dimension)
                    .background(
                        color = backgroundColor,
                        shape = PrezelTheme.shapes.V1000,
                    ),
            )
        }

        PrezelBadgeSize.REGULAR -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .size(dimension)
                    .background(
                        color = backgroundColor,
                        shape = PrezelTheme.shapes.V1000,
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp),
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
    }
}

@Composable
private fun badgeColors(
    disabled: PrezelBadgeDisabled,
    active: PrezelBadgeActive,
): Pair<Color, Color> =
    when {
        disabled == PrezelBadgeDisabled.ON -> PrezelTheme.colors.bgDisabled to PrezelTheme.colors.textDisabled
        active == PrezelBadgeActive.ON -> PrezelTheme.colors.interactiveRegular to PrezelTheme.colors.textLarge
        else -> PrezelTheme.colors.solidBlack to PrezelTheme.colors.textLarge
    }

@ThemePreview
@Composable
fun PrezelBadgePreview() {
    PrezelTheme {
        Column(
            modifier = Modifier
                .background(PrezelTheme.colors.bgRegular)
                .padding(16.dp),
        ) {
            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                PrezelBadge(
                    size = PrezelBadgeSize.SMALL,
                    active = PrezelBadgeActive.OFF,
                    disabled = PrezelBadgeDisabled.OFF,
                )
            }

            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                PrezelBadge(
                    size = PrezelBadgeSize.SMALL,
                    active = PrezelBadgeActive.ON,
                    disabled = PrezelBadgeDisabled.OFF,
                )
            }

            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                PrezelBadge(
                    size = PrezelBadgeSize.SMALL,
                    active = PrezelBadgeActive.OFF,
                    disabled = PrezelBadgeDisabled.ON,
                )
            }

            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                PrezelBadge(
                    size = PrezelBadgeSize.REGULAR,
                    text = "0",
                    active = PrezelBadgeActive.OFF,
                    disabled = PrezelBadgeDisabled.OFF,
                )
            }

            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                PrezelBadge(
                    size = PrezelBadgeSize.REGULAR,
                    text = "1",
                    active = PrezelBadgeActive.ON,
                    disabled = PrezelBadgeDisabled.OFF,
                )
            }

            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                PrezelBadge(
                    size = PrezelBadgeSize.REGULAR,
                    text = "9",
                    active = PrezelBadgeActive.OFF,
                    disabled = PrezelBadgeDisabled.ON,
                )
            }
        }
    }
}
