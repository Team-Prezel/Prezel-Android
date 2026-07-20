package com.team.prezel.feature.report.impl.accuracydetail.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
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
        title = stringResource(R.string.feature_report_impl_section_accuracy),
    ) {
        TrailingIcon(
            iconResId = PrezelIcons.Cancel,
            contentDescription = stringResource(R.string.feature_report_impl_close),
            onClick = onClose,
        )
    }
}

@BasicPreview
@Composable
private fun AccuracyDetailTopAppBarPreview() {
    PrezelTheme {
        AccuracyDetailTopAppBar(onClose = {})
    }
}
