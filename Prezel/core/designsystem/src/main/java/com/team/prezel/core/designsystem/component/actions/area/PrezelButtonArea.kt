package com.team.prezel.core.designsystem.component.actions.area

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.PrezelDividerType
import com.team.prezel.core.designsystem.component.PrezelHorizontalDivider
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefault
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.SectionTitle
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Immutable
data class ButtonAreaButtonSpec(
    @param:DrawableRes val iconResId: Int? = null,
    val label: String,
    val enabled: Boolean = true,
    val onClick: () -> Unit,
)

@Composable
fun PrezelButtonArea(
    mainButton: ButtonAreaButtonSpec,
    modifier: Modifier = Modifier,
    subButton: ButtonAreaButtonSpec? = null,
    isVertical: Boolean = true,
    showBackground: Boolean = false,
    isStrongStrength: Boolean = true,
    isNested: Boolean = false,
    config: PrezelButtonAreaDefault = PrezelButtonAreaDefaults.getDefault(
        showBackground = showBackground,
        isNested = isNested,
    ),
) {
    Column(modifier = modifier) {
        if (showBackground) PrezelHorizontalDivider(type = PrezelDividerType.THICK)

        val contentModifier = Modifier
            .background(config.backgroundColor)
            .padding(config.contentPadding)
            .fillMaxWidth()

        if (isVertical) {
            ButtonAreaVertical(
                modifier = contentModifier,
                mainButton = mainButton,
                subButton = subButton,
                buttonAreaDefault = config,
            )
        } else {
            ButtonAreaHorizontal(
                modifier = contentModifier,
                isStrongStrength = isStrongStrength,
                mainButton = mainButton,
                subButton = subButton,
                buttonAreaDefault = config,
            )
        }
    }
}

@Composable
private fun ButtonAreaVertical(
    modifier: Modifier,
    mainButton: ButtonAreaButtonSpec,
    subButton: ButtonAreaButtonSpec?,
    buttonAreaDefault: PrezelButtonAreaDefault,
) {
    Column(modifier = modifier) {
        PrezelButton(
            modifier = Modifier.fillMaxWidth(),
            text = mainButton.label,
            iconResId = mainButton.iconResId,
            onClick = mainButton.onClick,
            enabled = mainButton.enabled,
            buttonDefault = buttonAreaDefault.mainButtonDefault.applyEnabled(mainButton.enabled),
        )

        if (subButton != null) {
            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))
            PrezelButton(
                modifier = Modifier.fillMaxWidth(),
                text = subButton.label,
                iconResId = subButton.iconResId,
                onClick = subButton.onClick,
                enabled = subButton.enabled,
                buttonDefault = buttonAreaDefault.subButtonDefault.applyEnabled(subButton.enabled),
            )
        }
    }
}

@Composable
private fun ButtonAreaHorizontal(
    modifier: Modifier,
    mainButton: ButtonAreaButtonSpec,
    subButton: ButtonAreaButtonSpec?,
    isStrongStrength: Boolean,
    buttonAreaDefault: PrezelButtonAreaDefault,
) {
    Row(modifier = modifier) {
        if (subButton != null) {
            PrezelButton(
                modifier = if (isStrongStrength) Modifier else Modifier.weight(1f),
                text = subButton.label,
                iconResId = subButton.iconResId,
                onClick = subButton.onClick,
                enabled = subButton.enabled,
                buttonDefault = buttonAreaDefault.subButtonDefault.applyEnabled(subButton.enabled),
            )
            Spacer(modifier = Modifier.width(PrezelTheme.spacing.V12))
        }

        PrezelButton(
            modifier = Modifier.weight(1f),
            text = mainButton.label,
            iconResId = mainButton.iconResId,
            onClick = mainButton.onClick,
            enabled = mainButton.enabled,
            buttonDefault = buttonAreaDefault.mainButtonDefault.applyEnabled(mainButton.enabled),
        )
    }
}

@Composable
private fun PrezelButtonDefault.applyEnabled(enabled: Boolean): PrezelButtonDefault {
    if (this.enabled == enabled) return this

    val resolvedStateDefault = PrezelButtonDefaults.getDefault(
        isIconOnly = false,
        type = type,
        size = size,
        hierarchy = hierarchy,
        enabled = enabled,
    )

    return copy(
        contentColor = resolvedStateDefault.contentColor,
        backgroundColor = resolvedStateDefault.backgroundColor,
        borderColor = resolvedStateDefault.borderColor,
        borderWidth = resolvedStateDefault.borderWidth,
        shape = resolvedStateDefault.shape,
        textStyle = resolvedStateDefault.textStyle,
        contentPadding = resolvedStateDefault.contentPadding,
        iconSpacing = resolvedStateDefault.iconSpacing,
        iconSize = resolvedStateDefault.iconSize,
    )
}

private data class ButtonAreaPreviewVariant(
    val title: String,
    val isVertical: Boolean,
    val isStrongStrength: Boolean,
)

private val buttonAreaPreviewVariants =
    listOf(
        ButtonAreaPreviewVariant(title = "Vertical", isVertical = true, isStrongStrength = true),
        ButtonAreaPreviewVariant(title = "Horizontal / Strong Strength", isVertical = false, isStrongStrength = true),
        ButtonAreaPreviewVariant(title = "Horizontal / Weak Strength", isVertical = false, isStrongStrength = false),
    )

private val buttonAreaPreviewStates =
    listOf(
        true to "Enabled",
        false to "Disabled",
    )

@Preview(device = "spec:width=1080dp,height=1800dp")
@Composable
private fun PrezelButtonAreaPreview() {
    PrezelTheme {
        PreviewScaffold {
            SectionTitle(title = "Button Area")
            Text(
                text = "행은 버튼 상태, 열은 배경 여부입니다. 각 섹션은 배치 방향과 강도 옵션을 구분합니다.",
                style = PrezelTheme.typography.body3Regular,
                color = PrezelTheme.colors.textMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray.copy(alpha = 0.4f))
                    .padding(8.dp),
            )

            buttonAreaPreviewVariants.forEach { variant ->
                ButtonAreaPreviewSection(variant = variant)
            }
        }
    }
}

@Composable
private fun ButtonAreaPreviewSection(variant: ButtonAreaPreviewVariant) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = variant.title,
            style = PrezelTheme.typography.body3Bold,
            color = PrezelTheme.colors.textLarge,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            ButtonAreaPreviewHeaderRow()

            buttonAreaPreviewStates.forEach { (enabled, label) ->
                ButtonAreaPreviewRow(
                    variant = variant,
                    enabled = enabled,
                    label = label,
                )
            }
        }
    }
}

@Composable
private fun ButtonAreaPreviewHeaderRow() {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        ButtonAreaPreviewHeaderCell(
            text = "State",
            modifier = Modifier.weight(0.5f),
        )
        ButtonAreaPreviewHeaderCell(
            text = "No Background",
            modifier = Modifier.weight(1f),
        )
        ButtonAreaPreviewHeaderCell(
            text = "Background",
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ButtonAreaPreviewRow(
    variant: ButtonAreaPreviewVariant,
    enabled: Boolean,
    label: String,
) {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        ButtonAreaPreviewCell(modifier = Modifier.weight(0.5f)) {
            Text(
                text = label,
                style = PrezelTheme.typography.body3Medium,
                color = PrezelTheme.colors.textLarge,
                textAlign = TextAlign.Center,
            )
        }

        ButtonAreaPreviewCell(modifier = Modifier.weight(1f)) {
            ButtonAreaPreviewSample(
                variant = variant,
                enabled = enabled,
                showBackground = false,
            )
        }

        ButtonAreaPreviewCell(modifier = Modifier.weight(1f)) {
            ButtonAreaPreviewSample(
                variant = variant,
                enabled = enabled,
                showBackground = true,
            )
        }
    }
}

@Composable
private fun ButtonAreaPreviewSample(
    variant: ButtonAreaPreviewVariant,
    enabled: Boolean,
    showBackground: Boolean,
) {
    val button = ButtonAreaButtonSpec(
        label = "Label",
        iconResId = PrezelIcons.Blank,
        enabled = enabled,
        onClick = {},
    )

    PrezelButtonArea(
        modifier = Modifier.fillMaxWidth(),
        isVertical = variant.isVertical,
        isStrongStrength = variant.isStrongStrength,
        showBackground = showBackground,
        mainButton = button,
        subButton = button,
    )
}

@Composable
private fun ButtonAreaPreviewHeaderCell(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .border(width = PrezelTheme.stroke.V1, color = PrezelTheme.colors.borderRegular)
            .background(PrezelTheme.colors.bgMedium)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = PrezelTheme.typography.body3Medium,
            color = PrezelTheme.colors.textMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ButtonAreaPreviewCell(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .border(width = PrezelTheme.stroke.V1, color = PrezelTheme.colors.borderRegular)
            .padding(horizontal = 12.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
