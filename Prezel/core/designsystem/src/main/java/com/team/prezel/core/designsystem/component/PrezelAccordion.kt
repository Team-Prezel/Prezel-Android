package com.team.prezel.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.R
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelAccordion(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    showDivider: Boolean = false,
    header: @Composable (expanded: Boolean) -> Unit,
    label: @Composable (expanded: Boolean) -> Unit,
    content: @Composable () -> Unit,
) {
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "accordionChevronRotation",
    )

    Surface(
        modifier = modifier,
        color = Color.Transparent,
    ) {
        Column {
            PrezelAccordionHeader(
                enabled = enabled,
                onClick = { onExpandedChange(!expanded) },
                header = { header(expanded) },
                label = { label(expanded) },
                chevron = { PrezelAccordionChevron(rotation = rotation) },
            )

            PrezelAccordionDivider(showDivider = showDivider)

            PrezelAccordionContent(
                expanded = expanded,
                content = content,
            )
        }
    }
}

@Composable
private fun PrezelAccordionHeader(
    enabled: Boolean,
    onClick: () -> Unit,
    header: @Composable () -> Unit,
    label: @Composable () -> Unit,
    chevron: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .clickable(
                enabled = enabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1f),
        ) {
            header()
        }
        Spacer(Modifier.width(8.dp))

        label()

        Spacer(Modifier.width(8.dp))

        chevron()
    }
}

@Composable
private fun PrezelAccordionChevron(rotation: Float) {
    Icon(
        painter = painterResource(PrezelIcons.ChevronDown),
        contentDescription = stringResource(R.string.core_designsystem_accordion_desc),
        modifier = Modifier
            .size(24.dp)
            .rotate(rotation),
        tint = PrezelTheme.colors.iconRegular,
    )
}

@Composable
private fun PrezelAccordionDivider(showDivider: Boolean) {
    if (!showDivider) return

    PrezelHorizontalDivider(
        type = PrezelDividerType.THICK,
        color = PrezelTheme.colors.borderSmall,
    )
}

@Composable
private fun PrezelAccordionContent(
    expanded: Boolean,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = expanded,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            content()
        }
    }
}

@ThemePreview
@Composable
private fun PrezelAccordionPreview_Collapsed() {
    PrezelTheme {
        PreviewScaffold {
            PrezelAccordion(
                expanded = false,
                onExpandedChange = {},
                showDivider = true,
                header = {
                    Text(
                        text = "Title",
                        style = PrezelTextStyles.Body2Medium.toTextStyle(),
                        color = PrezelTheme.colors.textLarge,
                    )
                },
                label = {
                    Text(
                        text = "Label",
                        style = PrezelTextStyles.Body2Bold.toTextStyle(),
                        color = PrezelTheme.colors.interactiveRegular,
                    )
                },
            ) {
                Text(
                    text = "Content",
                    style = PrezelTextStyles.Body3Regular.toTextStyle(),
                    color = PrezelTheme.colors.textRegular,
                )
            }
        }
    }
}

@ThemePreview
@Composable
private fun PrezelAccordionPreview_Expanded() {
    PrezelTheme {
        PreviewScaffold {
            PrezelAccordion(
                expanded = true,
                onExpandedChange = {},
                showDivider = true,
                header = {
                    Text(
                        text = "Title",
                        modifier = Modifier.padding(start = 12.dp),
                        style = PrezelTextStyles.Body2Medium.toTextStyle(),
                        color = PrezelTheme.colors.textLarge,
                    )
                },
                label = {
                    Text(
                        text = "Label",
                        style = PrezelTextStyles.Body2Bold.toTextStyle(),
                        color = PrezelTheme.colors.interactiveRegular,
                    )
                },
            ) {
                Text(
                    text = "Content 영역입니다.\n여기에 설명이나 리스트가 들어갑니다.",
                    style = PrezelTextStyles.Caption2Medium.toTextStyle(),
                    color = PrezelTheme.colors.textLarge,
                )
            }
        }
    }
}

@ThemePreview
@Composable
private fun PrezelAccordionPreview_Interactive() {
    PrezelTheme {
        var expanded by remember { mutableStateOf(false) }

        PreviewScaffold {
            PrezelAccordion(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                showDivider = true,
                header = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        PrezelCheckbox(
                            checked = false,
                            size = CheckboxSize.REGULAR,
                            onCheckedChange = {},
                        )

                        Spacer(Modifier.width(4.dp))

                        Text(text = "(필수) 이용약관")
                    }
                },
                label = {
                    Text(
                        text = "자세히 보기",
                        style = PrezelTextStyles.Caption2Medium.toTextStyle(),
                        color = PrezelTheme.colors.textMedium,
                    )
                },
            ) {
                Text(
                    text = "클릭하면 열리고 닫힙니다.",
                    style = PrezelTextStyles.Caption2Medium.toTextStyle(),
                    color = PrezelTheme.colors.textLarge,
                )
            }
        }
    }
}
