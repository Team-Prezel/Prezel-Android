package com.team.prezel.core.designsystem.component.actions.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefault
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.component.actions.button.config.buttonContentPadding
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.SectionTitle
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.designsystem.util.drawDashBorder

@Composable
fun PrezelTextButton(
    text: String,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.FILLED,
    size: ButtonSize = ButtonSize.REGULAR,
    hierarchy: ButtonHierarchy = ButtonHierarchy.PRIMARY,
    enabled: Boolean = true,
    isRounded: Boolean = false,
    onClick: () -> Unit,
    buttonDefault: PrezelButtonDefault = PrezelButtonDefaults.getDefault(
        isIconOnly = false,
        isRounded = isRounded,
        type = type,
        size = size,
        hierarchy = hierarchy,
        enabled = enabled,
    ),
) {
    PrezelTouchArea(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = buttonDefault.shape,
        isUseRipple = true,
        content = { PrezelTextButtonContent(text = text, buttonDefault = buttonDefault) },
    )
}

@Composable
private fun PrezelTextButtonContent(
    text: String,
    modifier: Modifier = Modifier,
    buttonDefault: PrezelButtonDefault,
) {
    val textColor by remember(buttonDefault.enabled, buttonDefault.contentColor) {
        mutableStateOf(buttonDefault.contentColor)
    }

    val backgroundColor by remember(buttonDefault.enabled, buttonDefault.backgroundColor) {
        mutableStateOf(buttonDefault.backgroundColor)
    }

    PrezelButtonLayout(
        modifier = modifier
            .clip(shape = buttonDefault.shape)
            .background(color = backgroundColor)
            .then(
                if (buttonDefault.type == ButtonType.OUTLINED) {
                    Modifier.border(
                        width = PrezelTheme.stroke.V1,
                        color = buttonDefault.borderColor,
                        shape = buttonDefault.shape,
                    )
                } else {
                    Modifier
                },
            ).buttonContentPadding(size = buttonDefault.size),
        horizontalArrangement = Arrangement.Center,
        text = {
            Text(
                text = text,
                style = buttonDefault.textStyle,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        },
    )
}

@Preview(device = "spec:width=1080dp,height=1350dp")
@Composable
private fun PrezelTextButtons() {
    PrezelTheme {
        PreviewScaffold {
            SectionTitle(title = "Button/Text")
            Text(
                text = "행은 Size, 열은 Type과 Hierarchy 조합입니다. 각 섹션은 Enabled와 Rounded 상태를 구분합니다.",
                style = PrezelTheme.typography.body3Regular,
                color = PrezelTheme.colors.textMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray.copy(alpha = 0.4f))
                    .padding(8.dp),
            )
            PrezelTextButtonTable(
                title = "Enabled / Default",
                enabled = true,
                isRounded = false,
            )
            PrezelTextButtonTable(
                title = "Enabled / Rounded",
                enabled = true,
                isRounded = true,
            )
            PrezelTextButtonTable(
                title = "Disabled / Default",
                enabled = false,
                isRounded = false,
            )
            PrezelTextButtonTable(
                title = "Disabled / Rounded",
                enabled = false,
                isRounded = true,
            )
        }
    }
}

@Composable
private fun PrezelTextButtonTable(
    title: String,
    enabled: Boolean,
    isRounded: Boolean,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = PrezelTheme.typography.body3Bold,
            color = PrezelTheme.colors.textLarge,
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            PrezelTextButtonTableHeader()

            ButtonSize.entries.forEach { size ->
                PrezelTextButtonTableRow(
                    size = size,
                    enabled = enabled,
                    isRounded = isRounded,
                )
            }
        }
    }
}

@Composable
private fun PrezelTextButtonTableHeader() {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        PreviewHeaderCell(
            text = "Size",
            modifier = Modifier.defaultMinSize(minWidth = 88.dp),
        )

        ButtonType.entries.forEach { type ->
            ButtonHierarchy.entries.forEach { hierarchy ->
                PreviewHeaderCell(
                    text = "${type.name}\n${hierarchy.name}",
                    modifier = Modifier.defaultMinSize(minWidth = 156.dp),
                )
            }
        }
    }
}

@Composable
private fun PrezelTextButtonTableRow(
    size: ButtonSize,
    enabled: Boolean,
    isRounded: Boolean,
) {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        PreviewButtonCell(modifier = Modifier.defaultMinSize(minWidth = 88.dp)) {
            Text(
                text = size.name.lowercase(),
                style = PrezelTheme.typography.body3Medium,
                color = PrezelTheme.colors.textLarge,
            )
        }

        ButtonType.entries.forEach { type ->
            ButtonHierarchy.entries.forEach { hierarchy ->
                PreviewButtonCell(modifier = Modifier.defaultMinSize(minWidth = 156.dp)) {
                    PrezelTextButton(
                        text = "Label",
                        type = type,
                        size = size,
                        hierarchy = hierarchy,
                        enabled = enabled,
                        isRounded = isRounded,
                        onClick = {},
                        modifier = if (type == ButtonType.GHOST) {
                            Modifier.drawDashBorder(
                                shape = PrezelButtonDefaults
                                    .getDefault(
                                        isIconOnly = false,
                                        isRounded = isRounded,
                                        type = type,
                                        size = size,
                                        hierarchy = hierarchy,
                                    ).shape,
                            )
                        } else {
                            Modifier
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun PreviewHeaderCell(
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
private fun PreviewButtonCell(
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
