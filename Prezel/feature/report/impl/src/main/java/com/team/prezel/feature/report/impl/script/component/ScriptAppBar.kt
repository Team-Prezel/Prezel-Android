package com.team.prezel.feature.report.impl.script.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScriptAppBar(
    title: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrezelTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = PrezelTheme.typography.body2Bold,
                color = PrezelTheme.colors.textLarge,
            )
        },
        trailingIcons = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    painter = painterResource(PrezelIcons.Cancel),
                    contentDescription = stringResource(R.string.feature_report_impl_close),
                    tint = PrezelTheme.colors.iconRegular,
                )
            }
        },
    )
}

@BasicPreview
@Composable
private fun ScriptAppBarPreview() {
    PrezelTheme {
        ScriptAppBar(
            title = "발표 대본",
            onCloseClick = {},
        )
    }
}
