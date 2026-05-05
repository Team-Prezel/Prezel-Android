package com.team.prezel.feature.my.impl.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.component.actions.button.PrezelIconButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.my.impl.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MyTopAppBar(onClickSetting: () -> Unit) {
    PrezelTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = { Text(text = stringResource(R.string.feature_my_impl_title)) },
        trailingIcons = {
            PrezelIconButton(
                iconResId = PrezelIcons.Setting,
                type = ButtonType.GHOST,
                hierarchy = ButtonHierarchy.SECONDARY,
                onClick = onClickSetting,
            )
        },
    )
}

@BasicPreview
@Composable
private fun MyTopAppBarPreview() {
    PrezelTheme {
        MyTopAppBar(
            onClickSetting = {},
        )
    }
}
