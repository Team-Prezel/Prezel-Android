package com.team.prezel.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.team.prezel.core.designsystem.foundation.typography.PrezelTextStyles
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.theme.PrezelTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrezelTopAppBar(
    @StringRes titleRes: Int,
    showTitle: Boolean,
    useSafeArea: Boolean,
    showLeadingIcon: Boolean,
    @DrawableRes leadingIcon: Int,
    leadingIconContentDescription: String,
    showTrailingIcon1: Boolean,
    @DrawableRes trailingIcon1: Int,
    trailingIcon1ContentDescription: String,
    showTrailingIcon2: Boolean,
    @DrawableRes trailingIcon2: Int,
    trailingIcon2ContentDescription: String,
    modifier: Modifier = Modifier,
    onLeadingIconClick: () -> Unit = {},
    onTrailingIcon1Click: () -> Unit = {},
    onTrailingIcon2Click: () -> Unit = {},
) {
    TopAppBar(
        title = {
            if (showTitle) {
                Text(
                    text = stringResource(id = titleRes),
                    style = PrezelTextStyles.Body2Bold.toTextStyle(),
                )
            }
        },
        navigationIcon = {
            if (showLeadingIcon) {
                IconButton(onClick = onLeadingIconClick) {
                    Icon(
                        painter = painterResource(id = leadingIcon),
                        contentDescription = leadingIconContentDescription,
                        tint = PrezelTheme.colors.iconRegular,
                    )
                }
            }
        },
        actions = {
            if (showTrailingIcon1) {
                IconButton(onClick = onTrailingIcon1Click) {
                    Icon(
                        painter = painterResource(id = trailingIcon1),
                        contentDescription = trailingIcon1ContentDescription,
                        tint = PrezelTheme.colors.iconRegular,
                    )
                }
            }
            if (showTrailingIcon2) {
                IconButton(onClick = onTrailingIcon2Click) {
                    Icon(
                        painter = painterResource(id = trailingIcon2),
                        contentDescription = trailingIcon2ContentDescription,
                        tint = PrezelTheme.colors.iconRegular,
                    )
                }
            }
        },
        windowInsets = if (useSafeArea) {
            TopAppBarDefaults.windowInsets
        } else {
            WindowInsets(0)
        },
        modifier = modifier.testTag("PrezelTopAppBar"),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Title Only")
@Composable
private fun PrezelTopAppBarTitleOnlyPreview() {
    PrezelTheme {
        PrezelTopAppBar(
            titleRes = android.R.string.untitled,
            showTitle = true,
            useSafeArea = false,
            showLeadingIcon = false,
            leadingIcon = PrezelIcons.Blank,
            leadingIconContentDescription = "",
            showTrailingIcon1 = false,
            trailingIcon1 = PrezelIcons.Blank,
            trailingIcon1ContentDescription = "",
            showTrailingIcon2 = false,
            trailingIcon2 = PrezelIcons.Blank,
            trailingIcon2ContentDescription = "",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("With Leading Icon")
@Composable
private fun PrezelTopAppBarWithLeadingPreview() {
    PrezelTheme {
        PrezelTopAppBar(
            titleRes = android.R.string.untitled,
            showTitle = true,
            useSafeArea = false,
            showLeadingIcon = true,
            leadingIcon = PrezelIcons.Blank,
            leadingIconContentDescription = "",
            showTrailingIcon1 = false,
            trailingIcon1 = PrezelIcons.Blank,
            trailingIcon1ContentDescription = "",
            showTrailingIcon2 = false,
            trailingIcon2 = PrezelIcons.Blank,
            trailingIcon2ContentDescription = "",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("With All Icons")
@Composable
private fun PrezelTopAppBarWithAllIconsPreview() {
    PrezelTheme {
        PrezelTopAppBar(
            titleRes = android.R.string.untitled,
            showTitle = true,
            useSafeArea = false,
            showLeadingIcon = true,
            leadingIcon = PrezelIcons.Blank,
            leadingIconContentDescription = "",
            showTrailingIcon1 = true,
            trailingIcon1 = PrezelIcons.Blank,
            trailingIcon1ContentDescription = "",
            showTrailingIcon2 = true,
            trailingIcon2 = PrezelIcons.Blank,
            trailingIcon2ContentDescription = "",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("With Trailing Icon1")
@Composable
private fun PrezelTopAppBarWithTrailingIcon1Preview() {
    PrezelTheme {
        PrezelTopAppBar(
            titleRes = android.R.string.untitled,
            showTitle = true,
            useSafeArea = false,
            showLeadingIcon = true,
            leadingIcon = PrezelIcons.Blank,
            leadingIconContentDescription = "",
            showTrailingIcon1 = true,
            trailingIcon1 = PrezelIcons.Blank,
            trailingIcon1ContentDescription = "",
            showTrailingIcon2 = false,
            trailingIcon2 = PrezelIcons.Blank,
            trailingIcon2ContentDescription = "",
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("TopAppBar - Safe Area On")
@Composable
private fun PrezelTopAppBarSafeAreaOnPreview() {
    PrezelTheme {
        Scaffold { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                PrezelTopAppBar(
                    titleRes = android.R.string.untitled,
                    showTitle = true,
                    useSafeArea = true,
                    showLeadingIcon = true,
                    leadingIcon = PrezelIcons.Blank,
                    leadingIconContentDescription = "",
                    showTrailingIcon1 = true,
                    trailingIcon1 = PrezelIcons.Blank,
                    trailingIcon1ContentDescription = "",
                    showTrailingIcon2 = false,
                    trailingIcon2 = PrezelIcons.Blank,
                    trailingIcon2ContentDescription = "",
                )
            }
        }
    }
}
