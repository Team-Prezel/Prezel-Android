package com.team.prezel.feature.report.impl.detail.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.R

@Composable
internal fun ReportBottomActionArea(onDeleteClick: () -> Unit) {
    PrezelButton(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(R.string.feature_report_impl_delete),
        type = ButtonType.OUTLINED,
        size = ButtonSize.REGULAR,
        hierarchy = ButtonHierarchy.SECONDARY,
        onClick = onDeleteClick,
    )
}

@BasicPreview
@Composable
private fun ReportBottomActionAreaPreview() {
    PrezelTheme {
        ReportBottomActionArea(
            onDeleteClick = {},
        )
    }
}
