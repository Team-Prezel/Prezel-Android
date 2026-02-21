package com.team.prezel.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.PrezelDividerType
import com.team.prezel.core.designsystem.component.PrezelHorizontalDivider
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.SectionTitle
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Immutable
data class ButtonAreaButtonSpec(
    val icon: IconSource? = null,
    val label: String,
    val enabled: Boolean = true,
    val onClick: () -> Unit,
)

@Composable
fun PrezelButtonArea(
    mainButton: ButtonAreaButtonSpec,
    subButton: ButtonAreaButtonSpec?,
    modifier: Modifier = Modifier,
    isVertical: Boolean = true,
    showBackground: Boolean = false,
    isStrongStrength: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(PrezelTheme.spacing.V20),
) {
    val buttonAreaModifier = modifier
        .background(if (showBackground) PrezelTheme.colors.bgRegular else Color.Transparent)
        .padding(contentPadding)
        .fillMaxWidth()

    Column {
        if (showBackground) {
            PrezelHorizontalDivider(type = PrezelDividerType.THICK)
        }
        if (isVertical) {
            ButtonAreaVertical(
                modifier = buttonAreaModifier,
                mainButton = mainButton,
                subButton = subButton,
            )
        } else {
            ButtonAreaHorizontal(
                modifier = buttonAreaModifier,
                isStrongStrength = isStrongStrength,
                mainButton = mainButton,
                subButton = subButton,
            )
        }
    }
}

@Composable
private fun ButtonAreaVertical(
    modifier: Modifier,
    mainButton: ButtonAreaButtonSpec,
    subButton: ButtonAreaButtonSpec?,
) {
    Column(modifier = modifier) {
        PrezelButton(
            modifier = Modifier.fillMaxWidth(),
            text = mainButton.label,
            icon = mainButton.icon,
            onClick = mainButton.onClick,
            enabled = mainButton.enabled,
            style = PrezelButtonStyle(buttonHierarchy = PrezelButtonHierarchy.PRIMARY),
        )

        if (subButton != null) {
            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))
            PrezelButton(
                modifier = Modifier.fillMaxWidth(),
                text = subButton.label,
                icon = subButton.icon,
                onClick = subButton.onClick,
                enabled = subButton.enabled,
                style = PrezelButtonStyle(buttonHierarchy = PrezelButtonHierarchy.SECONDARY),
            )
        }
    }
}

@Composable
private fun ButtonAreaHorizontal(
    modifier: Modifier,
    isStrongStrength: Boolean,
    mainButton: ButtonAreaButtonSpec,
    subButton: ButtonAreaButtonSpec?,
) {
    Row(modifier = modifier) {
        val mainModifier = Modifier.weight(1f)
        val subModifier = if (isStrongStrength) Modifier else Modifier.weight(1f)

        if (subButton != null) {
            PrezelButton(
                modifier = subModifier,
                text = subButton.label,
                icon = subButton.icon,
                onClick = subButton.onClick,
                enabled = subButton.enabled,
                style = PrezelButtonStyle(buttonHierarchy = PrezelButtonHierarchy.SECONDARY),
            )
            Spacer(modifier = Modifier.width(PrezelTheme.spacing.V12))
        }

        PrezelButton(
            modifier = mainModifier,
            text = mainButton.label,
            icon = mainButton.icon,
            onClick = mainButton.onClick,
            enabled = mainButton.enabled,
            style = PrezelButtonStyle(buttonHierarchy = PrezelButtonHierarchy.PRIMARY),
        )
    }
}

@ThemePreview
@Composable
private fun PrezelButtonAreaVerticalPreview() {
    PrezelButtonAreaTypedPreview(title = "Vertical", isVertical = true, isStrongStrength = true)
}

@ThemePreview
@Composable
private fun PrezelButtonAreaHorizontalStrongPreview() {
    PrezelButtonAreaTypedPreview(title = "Horizontal & Strong Strength", isVertical = false, isStrongStrength = true)
}

@ThemePreview
@Composable
private fun PrezelButtonAreaHorizontalWeakPreview() {
    PrezelButtonAreaTypedPreview(title = "Horizontal & Weak Strength", isVertical = false, isStrongStrength = false)
}

@Composable
private fun PrezelButtonAreaTypedPreview(
    title: String,
    isVertical: Boolean,
    isStrongStrength: Boolean,
) {
    val mainButton = ButtonAreaButtonSpec(
        label = "Main Button",
        icon = IconSource(resId = PrezelIcons.Blank),
        onClick = {},
    )
    val subButton = ButtonAreaButtonSpec(
        label = "Sub",
        icon = IconSource(resId = PrezelIcons.Blank),
        onClick = {},
    )

    PrezelTheme {
        Column(
            modifier = Modifier.background(PrezelTheme.colors.bgMedium),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SectionTitle(title = title)
            PrezelButtonArea(
                isVertical = isVertical,
                isStrongStrength = isStrongStrength,
                showBackground = false,
                mainButton = mainButton,
                subButton = subButton,
            )
            PrezelButtonArea(
                isVertical = isVertical,
                isStrongStrength = isStrongStrength,
                showBackground = true,
                mainButton = mainButton,
                subButton = subButton,
            )
        }
    }
}
