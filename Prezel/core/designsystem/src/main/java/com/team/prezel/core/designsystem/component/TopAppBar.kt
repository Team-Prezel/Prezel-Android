package com.team.prezel.core.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.ThemePreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrezelTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    leadingIcon: @Composable () -> Unit = {},
    trailingIcons: @Composable RowScope.() -> Unit = {},
    isScrolled: Boolean = false,
) {
    TopAppBar(
        title = title,
        navigationIcon = leadingIcon,
        actions = trailingIcons,
        colors = if (isScrolled) {
            PrezelTopAppBarDefaults.scrolledColors()
        } else {
            PrezelTopAppBarDefaults.colors()
        },
        modifier = modifier.testTag("PrezelTopAppBar"),
    )
}

object PrezelTopAppBarDefaults {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun colors() =
        TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        )

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun scrolledColors() =
        TopAppBarDefaults.topAppBarColors(
            containerColor = PrezelTheme.colors.bgRegular,
        )
}

@ThemePreview
@Composable
private fun PrezelTopAppBarTitleOnlyPreview() {
    PrezelTheme {
        PrezelTopAppBar(
            title = {
                Text(
                    text = "제목",
                    style = PrezelTextStyles.Body2Bold.toTextStyle(),
                )
            },
        )
    }
}

@ThemePreview
@Composable
private fun PrezelTopAppBarWithLeadingPreview() {
    PrezelTheme {
        PrezelTopAppBar(
            title = {
                Text(
                    text = "제목",
                    style = PrezelTextStyles.Body2Bold.toTextStyle(),
                )
            },
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(PrezelIcons.Blank),
                        contentDescription = "뒤로가기",
                        tint = PrezelTheme.colors.iconRegular,
                    )
                }
            },
        )
    }
}

@ThemePreview
@Composable
private fun PrezelTopAppBarWithAllIconsPreview() {
    PrezelTheme {
        PrezelTopAppBar(
            title = {
                Text(
                    text = "제목",
                    style = PrezelTextStyles.Body2Bold.toTextStyle(),
                )
            },
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(PrezelIcons.Blank),
                        contentDescription = "뒤로가기",
                        tint = PrezelTheme.colors.iconRegular,
                    )
                }
            },
            trailingIcons = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(PrezelIcons.Blank),
                        contentDescription = "검색",
                        tint = PrezelTheme.colors.iconRegular,
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(PrezelIcons.Blank),
                        contentDescription = "더보기",
                        tint = PrezelTheme.colors.iconRegular,
                    )
                }
            },
        )
    }
}

@ThemePreview
@Composable
private fun PrezelTopAppBarScrolledPreview() {
    PrezelTheme {
        PrezelTopAppBar(
            title = {
                Text(
                    text = "제목",
                    style = PrezelTextStyles.Body2Bold.toTextStyle(),
                )
            },
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(PrezelIcons.Blank),
                        contentDescription = "뒤로가기",
                        tint = PrezelTheme.colors.iconRegular,
                    )
                }
            },
            isScrolled = true,
        )
    }
}
