package com.team.prezel.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelCheckbox(
    size: CheckboxSize = CheckboxSize.REGULAR,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
) {
    var currentState by remember { mutableStateOf(checked) }

    val checkboxSize = when (size) {
        CheckboxSize.REGULAR -> 24.dp
        CheckboxSize.LARGE -> 32.dp
    }

    val iconRes = if (currentState) R.drawable.ic_check_circle_filled else R.drawable.ic_check_circle_outlined
    val iconColor = if (currentState) PrezelTheme.colors.interactiveRegular else PrezelTheme.colors.iconDisabled

    Box(
        modifier = Modifier
            .size(checkboxSize)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                currentState = !currentState
                onCheckedChange(currentState)
            },
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

enum class CheckboxSize {
    REGULAR,
    LARGE,
}

@ThemePreview
@Composable
private fun PrezelRegularCheckboxPreview() {
    PrezelTheme {
        Column(modifier = Modifier.background(PrezelTheme.colors.bgRegular)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp),
            ) {
                var regularChecked by remember { mutableStateOf(true) }
                var regularUnchecked by remember { mutableStateOf(false) }

                PrezelCheckbox(
                    size = CheckboxSize.REGULAR,
                    checked = regularChecked,
                    onCheckedChange = { regularChecked = it },
                )

                PrezelCheckbox(
                    size = CheckboxSize.REGULAR,
                    checked = regularUnchecked,
                    onCheckedChange = { regularUnchecked = it },
                )
            }
        }
    }
}

@ThemePreview
@Composable
private fun PrezelLargeCheckboxPreview() {
    PrezelTheme {
        Column(modifier = Modifier.background(PrezelTheme.colors.bgRegular)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp),
            ) {
                var largeChecked by remember { mutableStateOf(true) }
                var largeUnchecked by remember { mutableStateOf(false) }

                PrezelCheckbox(
                    size = CheckboxSize.LARGE,
                    checked = largeChecked,
                    onCheckedChange = { largeChecked = it },
                )

                PrezelCheckbox(
                    size = CheckboxSize.LARGE,
                    checked = largeUnchecked,
                    onCheckedChange = { largeUnchecked = it },
                )
            }
        }
    }
}
