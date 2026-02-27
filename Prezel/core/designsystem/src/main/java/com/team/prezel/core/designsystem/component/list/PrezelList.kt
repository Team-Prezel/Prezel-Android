package com.team.prezel.core.designsystem.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.icon.IconSource
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.PreviewScaffold
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PrezelList(
    title: String,
    modifier: Modifier = Modifier,
    size: PrezelListSize = PrezelListSize.REGULAR,
    nested: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    showLeadingContent: Boolean = false,
    leadingContent: @Composable () -> Unit = {},
    showTrailingContent: Boolean = true,
    showFirstTrailingContent: Boolean = true,
    trailingContents: ImmutableList<@Composable () -> Unit> = persistentListOf(),
) {
    val visibleTrailingContents =
        if (!showFirstTrailingContent) trailingContents.take(1) else trailingContents

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = PrezelTheme.spacing.V12, vertical = prezelListVerticalPadding(size = size)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (nested) {
            Spacer(modifier = Modifier.width(PrezelTheme.spacing.V12))
        }

        if (showLeadingContent) {
            leadingContent()
            Spacer(modifier = Modifier.width(prezelListIconTextSpacing(size = size)))
        }

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = LocalContentColor.current,
            maxLines = 1,
            style = prezelListTextStyle(size = size),
        )
        Spacer(modifier = Modifier.width(prezelListTextTrailingSpacing(size = size)))

        if (showTrailingContent) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    prezelListTrailingIconSpacing(size),
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                visibleTrailingContents.forEach { it() }
            }
        }
    }
}

@ThemePreview
@Composable
private fun PrezelListPreview() {
    PrezelTheme {
        PreviewScaffold {
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                PrezelList(
                    title = "Small Title",
                    size = PrezelListSize.SMALL,
                    showLeadingContent = true,
                    leadingContent = {
                        PrezelListIcon(
                            icon = IconSource(resId = PrezelIcons.Blank),
                            size = PrezelListSize.SMALL,
                        )
                    },
                    showFirstTrailingContent = false,
                    trailingContents = persistentListOf(
                        {
                            PrezelListIcon(
                                icon = IconSource(resId = PrezelIcons.Blank),
                                size = PrezelListSize.SMALL,
                            )
                        },
                        {
                            PrezelListIcon(
                                icon = IconSource(resId = PrezelIcons.Blank),
                                size = PrezelListSize.SMALL,
                            )
                        },
                    ),
                )
                PrezelList(
                    title = "Small Title 2",
                    size = PrezelListSize.SMALL,
                    leadingContent = {
                        PrezelListIcon(
                            icon = IconSource(resId = PrezelIcons.Blank),
                            size = PrezelListSize.SMALL,
                        )
                    },
                    trailingContents = persistentListOf(
                        {
                            PrezelListIcon(
                                icon = IconSource(resId = PrezelIcons.Blank),
                                size = PrezelListSize.SMALL,
                            )
                        },
                        {
                            PrezelListIcon(
                                icon = IconSource(resId = PrezelIcons.Blank),
                                size = PrezelListSize.SMALL,
                            )
                        },
                    ),
                )

                PrezelList(
                    title = "Regular Title",
                    size = PrezelListSize.REGULAR,
                    showLeadingContent = true,
                    leadingContent = {
                        PrezelListIcon(
                            icon = IconSource(resId = PrezelIcons.Blank),
                            size = PrezelListSize.REGULAR,
                        )
                    },
                    showFirstTrailingContent = false,
                    trailingContents = persistentListOf(
                        {
                            PrezelListIcon(
                                icon = IconSource(resId = PrezelIcons.Blank),
                                size = PrezelListSize.REGULAR,
                            )
                        },
                        {
                            PrezelListIcon(
                                icon = IconSource(resId = PrezelIcons.Blank),
                                size = PrezelListSize.REGULAR,
                            )
                        },
                    ),
                )
                PrezelList(
                    title = "Regular Title 2",
                    size = PrezelListSize.REGULAR,
                    leadingContent = {
                        PrezelListIcon(
                            icon = IconSource(resId = PrezelIcons.Blank),
                            size = PrezelListSize.REGULAR,
                        )
                    },
                    trailingContents = persistentListOf(
                        {
                            PrezelListIcon(
                                icon = IconSource(resId = PrezelIcons.Blank),
                                size = PrezelListSize.REGULAR,
                            )
                        },
                        {
                            PrezelListIcon(
                                icon = IconSource(resId = PrezelIcons.Blank),
                                size = PrezelListSize.REGULAR,
                            )
                        },
                    ),
                )
            }
        }
    }
}
