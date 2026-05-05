package com.team.prezel.core.designsystem.component.actions.area

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.PrezelDividerType
import com.team.prezel.core.designsystem.component.PrezelHorizontalDivider
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewSection
import com.team.prezel.core.designsystem.theme.PrezelTheme

/**
 * 하단 액션 영역에 주요 버튼과 보조 버튼을 함께 배치합니다.
 *
 * `content`에서는 `MainButton`을 먼저 선언하고 이어서 보조 버튼 하나를 추가합니다.
 * `isVertical`과 `showBackground` 조합에 따라 배치와 기본 스타일이 함께 달라집니다.
 */
@Composable
fun PrezelButtonArea(
    modifier: Modifier = Modifier,
    isVertical: Boolean = true,
    isStrongStrength: Boolean = true,
    showBackground: Boolean = false,
    isNested: Boolean = false,
    config: PrezelButtonAreaDefault = PrezelButtonAreaDefaults.getDefault(),
    mainButton: @Composable (Modifier) -> Unit,
    subButton: @Composable ((Modifier) -> Unit)? = null,
) {
    val contentModifier = if (isNested) Modifier else Modifier.padding(config.contentPadding)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(if (showBackground) Modifier.background(config.backgroundColor) else Modifier),
    ) {
        if (showBackground) {
            PrezelHorizontalDivider(type = PrezelDividerType.THICK, color = config.borderColor)
        }

        if (isVertical) {
            ButtonAreaVertical(
                mainButton = mainButton,
                subButton = subButton,
                modifier = contentModifier,
            )
        } else {
            ButtonAreaHorizontal(
                isStrongStrength = isStrongStrength,
                mainButton = mainButton,
                subButton = subButton,
                modifier = contentModifier,
            )
        }
    }
}

@Composable
private fun ButtonAreaVertical(
    mainButton: @Composable (Modifier) -> Unit,
    subButton: @Composable ((Modifier) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        mainButton(Modifier.fillMaxWidth())

        if (subButton != null) {
            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V12))
            subButton(Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun ButtonAreaHorizontal(
    isStrongStrength: Boolean,
    mainButton: @Composable (Modifier) -> Unit,
    subButton: @Composable ((Modifier) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        if (subButton != null) {
            subButton(if (isStrongStrength) Modifier else Modifier.weight(1f))
            Spacer(modifier = Modifier.width(PrezelTheme.spacing.V12))
        }

        mainButton(Modifier.weight(1f))
    }
}

private data class ButtonAreaPreviewVariant(
    val isVertical: Boolean,
    val isStrongStrength: Boolean,
)

private val buttonAreaPreviewStates =
    listOf(
        true to "Enabled",
        false to "Disabled",
    )

@Preview(device = "spec:width=1080dp,height=1800dp")
@Composable
private fun PrezelButtonAreaVerticalPreview() {
    PreviewSection(
        title = "Button Area - Vertical",
        description = "행은 버튼 상태, 열은 배경 여부입니다. 각 섹션은 배치 방향과 강도 옵션을 구분합니다.",
    ) {
        Column {
            ButtonAreaPreviewHeaderRow()

            buttonAreaPreviewStates.forEach { (enabled, label) ->
                ButtonAreaPreviewRow(
                    variant = ButtonAreaPreviewVariant(
                        isVertical = true,
                        isStrongStrength = false,
                    ),
                    enabled = enabled,
                    label = label,
                )
            }
        }
    }
}

@Preview(device = "spec:width=1080dp,height=1800dp")
@Composable
private fun PrezelButtonAreaHorizontalPreview() {
    PreviewSection(
        title = "Button Area - Horizontal",
        description = "행은 버튼 상태, 열은 배경 여부입니다. 각 섹션은 배치 방향과 강도 옵션을 구분합니다.",
    ) {
        Column {
            ButtonAreaPreviewHeaderRow()

            buttonAreaPreviewStates.forEach { (enabled, label) ->
                ButtonAreaPreviewRow(
                    variant = ButtonAreaPreviewVariant(
                        isVertical = false,
                        isStrongStrength = true,
                    ),
                    enabled = enabled,
                    label = label,
                )
                ButtonAreaPreviewRow(
                    variant = ButtonAreaPreviewVariant(
                        isVertical = false,
                        isStrongStrength = false,
                    ),
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
    PrezelButtonArea(
        modifier = Modifier.fillMaxWidth(),
        isVertical = variant.isVertical,
        isStrongStrength = variant.isStrongStrength,
        showBackground = showBackground,
        mainButton = { modifier ->
            PrezelButton(
                modifier = modifier,
                text = "Label",
                iconResId = PrezelIcons.Blank,
                onClick = { },
                enabled = enabled,
                type = ButtonType.FILLED,
                hierarchy = ButtonHierarchy.PRIMARY,
            )
        },
        subButton = { modifier ->
            PrezelButton(
                modifier = modifier,
                text = "Label",
                iconResId = PrezelIcons.Blank,
                onClick = { },
                enabled = enabled,
                type = ButtonType.FILLED,
                hierarchy = ButtonHierarchy.SECONDARY,
            )
        },
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
