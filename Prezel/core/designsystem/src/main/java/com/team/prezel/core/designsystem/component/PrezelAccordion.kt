package com.team.prezel.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.R
import com.team.prezel.core.designsystem.component.list.PrezelList
import com.team.prezel.core.designsystem.component.list.PrezelListSize
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
fun PrezelAccordion(
    title: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showDivider: Boolean = false,
    size: PrezelListSize = PrezelListSize.REGULAR,
    nested: Boolean = false,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
    ) {
        PrezelList(
            title = title,
            modifier = Modifier
                .clickable(
                    enabled = enabled,
                    indication = null,
                    interactionSource = null,
                ) {
                    onExpandedChange(!expanded)
                },
            size = size,
            nested = nested,
            leadingContent = leadingContent,
            trailingContent = {
                trailingContent?.invoke(this)
                PrezelAccordionChevron(expanded = expanded)
            },
        )

        if (showDivider) {
            PrezelHorizontalDivider(
                type = PrezelDividerType.THICK,
                color = PrezelTheme.colors.borderSmall,
            )
        }

        if (content != null) {
            PrezelAccordionContent(
                expanded = expanded,
                content = content,
            )
        }
    }
}

@Composable
private fun PrezelAccordionChevron(expanded: Boolean) {
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "accordionChevronRotation",
    )

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
            modifier = Modifier.fillMaxWidth(),
        ) {
            content()
        }
    }
}

@ThemePreview
@Composable
private fun PrezelAccordionPreview() {
    PrezelTheme {
        var expanded by remember { mutableStateOf(false) }

        PreviewScaffold {
            Column(verticalArrangement = Arrangement.spacedBy(40.dp)) {
                PrezelAccordion(
                    title = "Collapsed Accordion",
                    expanded = false,
                    onExpandedChange = {},
                    showDivider = true,
                    content = {
                        Text(
                            text = "Content",
                            style = PrezelTextStyles.Body3Regular.toTextStyle(),
                            color = PrezelTheme.colors.textRegular,
                        )
                    },
                )

                PrezelAccordion(
                    title = "Expanded Accordion",
                    expanded = true,
                    onExpandedChange = {},
                    showDivider = true,
                    content = {
                        Text(
                            text = "Content 영역입니다.\n여기에 설명이나 리스트가 들어갑니다.",
                            modifier = Modifier.padding(all = 12.dp),
                            color = PrezelTheme.colors.textLarge,
                            style = PrezelTextStyles.Caption2Medium.toTextStyle(),
                        )
                    },
                )

                PrezelAccordion(
                    title = "(필수) 이용약관",
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    showDivider = false,
                    leadingContent = {
                        PrezelCheckbox(
                            checked = false,
                            size = CheckboxSize.REGULAR,
                            onCheckedChange = {},
                        )
                    },
                    trailingContent = {
                        Text(
                            text = "자세히 보기",
                            color = PrezelTheme.colors.textMedium,
                            style = PrezelTextStyles.Caption2Medium.toTextStyle(),
                        )
                    },
                    content = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(PrezelTheme.colors.bgMedium)
                                .padding(all = 12.dp),
                        ) {
                            Text(
                                text = "본 약관은 서비스 이용과 관련한 기본적인 권리·의무 및 책임사항을 규정합니다.",
                                color = PrezelTheme.colors.textLarge,
                                style = PrezelTextStyles.Caption2Regular.toTextStyle(),
                            )
                        }
                    },
                )
            }
        }
    }
}
