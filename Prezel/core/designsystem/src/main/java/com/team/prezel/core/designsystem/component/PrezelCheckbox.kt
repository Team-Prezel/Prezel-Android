package com.team.prezel.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.R
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

enum class CheckboxSize {
    REGULAR,
    LARGE,
}

@Composable
fun PrezelCheckbox(
    modifier: Modifier = Modifier,
    size: CheckboxSize = CheckboxSize.REGULAR,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
) {
    val checkboxSize = when (size) {
        CheckboxSize.REGULAR -> 24.dp
        CheckboxSize.LARGE -> 32.dp
    }

    val iconRes =
        if (checked) {
            R.drawable.ic_check_circle_filled
        } else {
            R.drawable.ic_check_circle_outlined
        }

    val iconColor =
        if (checked) {
            PrezelTheme.colors.interactiveRegular
        } else {
            PrezelTheme.colors.iconDisabled
        }

    Box(
        modifier = modifier
            .size(checkboxSize)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onCheckedChange(!checked) },
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = "checkBox",
            modifier = Modifier.size(checkboxSize),
            tint = iconColor,
        )
    }
}

@ThemePreview
@Composable
private fun PrezelRegularCheckboxPreview() {
    PrezelTheme {
        Column(
            modifier = Modifier
                .background(PrezelTheme.colors.bgRegular)
                .padding(16.dp),
        ) {
            CheckboxRowPreview(title = "Regular Checkbox") {
                var checkState by remember { mutableStateOf(true) }

                PrezelCheckbox(
                    size = CheckboxSize.REGULAR,
                    checked = checkState,
                    onCheckedChange = { checkState = it },
                )

                PrezelCheckbox(
                    size = CheckboxSize.REGULAR,
                    checked = !checkState,
                    onCheckedChange = { checkState = it },
                )
            }
        }
    }
}

@ThemePreview
@Composable
private fun PrezelLargeCheckboxPreview() {
    PrezelTheme {
        Column(
            modifier = Modifier
                .background(PrezelTheme.colors.bgRegular)
                .padding(16.dp),
        ) {
            CheckboxRowPreview(title = "Large Checkbox") {
                var checkState by remember { mutableStateOf(true) }

                PrezelCheckbox(
                    size = CheckboxSize.LARGE,
                    checked = checkState,
                    onCheckedChange = { checkState = it },
                )

                PrezelCheckbox(
                    size = CheckboxSize.LARGE,
                    checked = !checkState,
                    onCheckedChange = { checkState = it },
                )
            }
        }
    }
}

@Composable
private fun CheckboxRowPreview(
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
