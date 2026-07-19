package com.team.prezel.feature.report.impl.script.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
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
        title = title,
    ) {
        TrailingIcon(
            iconResId = com.team.prezel.core.designsystem.icon.PrezelIcons.Cancel,
            contentDescription = stringResource(R.string.feature_report_impl_close),
            onClick = onCloseClick,
        )
    }
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
