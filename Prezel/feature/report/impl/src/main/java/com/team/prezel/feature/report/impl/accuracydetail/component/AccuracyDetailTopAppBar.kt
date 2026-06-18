package com.team.prezel.feature.report.impl.accuracydetail.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun AccuracyDetailTopAppBar(onClose: () -> Unit) {
    PrezelTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.feature_report_impl_section_accuracy),
                color = PrezelTheme.colors.textLarge,
            )
        },
        trailingIcons = {
            IconButton(onClick = onClose) {
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
private fun AccuracyDetailTopAppBarPreview() {
    PrezelTheme {
        AccuracyDetailTopAppBar(onClose = {})
    }
}
