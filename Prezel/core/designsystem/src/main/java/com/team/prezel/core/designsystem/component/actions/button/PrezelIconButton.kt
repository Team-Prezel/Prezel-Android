package com.team.prezel.core.designsystem.component.actions.button

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefault
import com.team.prezel.core.designsystem.component.actions.button.config.PrezelButtonDefaults
import com.team.prezel.core.designsystem.component.actions.button.config.iconButtonContentPadding
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.SectionTitle
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.designsystem.util.drawDashBorder

@Composable
fun PrezelIconButton(
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.FILLED,
    size: ButtonSize = ButtonSize.REGULAR,
    hierarchy: ButtonHierarchy = ButtonHierarchy.PRIMARY,
    enabled: Boolean = true,
    isRounded: Boolean = false,
    onClick: () -> Unit,
    buttonDefault: PrezelButtonDefault = PrezelButtonDefaults.getDefault(
        isIconOnly = true,
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
        content = {
            PrezelIconButtonContent(iconResId = iconResId, buttonDefault = buttonDefault)
        },
    )
}

@Composable
private fun PrezelIconButtonContent(
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier,
    buttonDefault: PrezelButtonDefault,
) {
    val contentColor by remember(buttonDefault.enabled, buttonDefault.contentColor) {
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
            ).iconButtonContentPadding(size = buttonDefault.size),
        horizontalArrangement = Arrangement.Center,
        leadingIcon = {
            PrezelButtonIcon(
                drawableRes = iconResId,
                tint = contentColor,
            )
        },
    )
}

@Preview(device = "spec:width=1080dp,height=1500dp")
@Composable
private fun PrezelIconButtons() {
    PrezelTheme {
        PreviewScaffold {
            SectionTitle(title = "Button/Icon")
            Text(
                text = "행은 Size, 열은 Type과 Hierarchy 조합입니다. 각 섹션은 Enabled와 Rounded 상태를 구분합니다.",
                style = PrezelTheme.typography.body3Regular,
                color = PrezelTheme.colors.textMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray.copy(alpha = 0.4f))
                    .padding(8.dp),
            )
            PrezelIconButtonTable(
                title = "Enabled / Default",
                enabled = true,
                isRounded = false,
            )
            PrezelIconButtonTable(
                title = "Enabled / Rounded",
                enabled = true,
                isRounded = true,
            )
            PrezelIconButtonTable(
                title = "Disabled / Default",
                enabled = false,
                isRounded = false,
            )
            PrezelIconButtonTable(
                title = "Disabled / Rounded",
                enabled = false,
                isRounded = true,
            )
        }
    }
}

@Composable
private fun PrezelIconButtonTable(
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
            PrezelIconButtonTableHeader()

            ButtonSize.entries.forEach { size ->
                PrezelIconButtonTableRow(
                    size = size,
                    enabled = enabled,
                    isRounded = isRounded,
                )
            }
        }
    }
}

@Composable
private fun PrezelIconButtonTableHeader() {
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
private fun PrezelIconButtonTableRow(
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
                    PrezelIconButton(
                        iconResId = PrezelIcons.Blank,
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
                                        isIconOnly = true,
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
