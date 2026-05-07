package com.team.prezel.feature.setting.impl.delete.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.setting.impl.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DeleteAccountTopAppBar(
    onClickClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PrezelTopAppBar(
        modifier = modifier,
        trailingIcons = {
            IconButton(onClick = onClickClose) {
                Icon(
                    painter = painterResource(PrezelIcons.Cancel),
                    contentDescription = stringResource(R.string.feature_setting_impl_close_icon_content_description),
                )
            }
        },
    )
}

@BasicPreview
@Composable
private fun DeleteAccountTopAppBarPreview() {
    PrezelTheme {
        DeleteAccountTopAppBar(onClickClose = {})
    }
}
