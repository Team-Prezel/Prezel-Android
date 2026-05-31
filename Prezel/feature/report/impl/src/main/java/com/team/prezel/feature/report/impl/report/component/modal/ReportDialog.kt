package com.team.prezel.feature.report.impl.report.component.modal

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.feedback.dialog.PrezelDialog
import com.team.prezel.core.designsystem.component.feedback.dialog.PrezelDialogScope
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.report.model.AnalysisReportDialog

@Composable
internal fun ReportDialog(
    type: AnalysisReportDialog,
    onDismissClick: () -> Unit,
    onConformClick: () -> Unit,
) {
    PrezelDialog(
        title = stringResource(type.titleResId),
        description = stringResource(type.descriptionResId),
        onDismiss = {},
    ) {
        Action(
            label = stringResource(R.string.feature_report_impl_dialog_cancel_action),
            onClick = onDismissClick,
        )

        Action(
            label = stringResource(type.actionTextResId),
            onClick = onConformClick,
            type = type.actionType,
        )
    }
}

private val AnalysisReportDialog.titleResId: Int
    @StringRes get() = when (this) {
        AnalysisReportDialog.RE_RECORDING -> R.string.feature_report_impl_re_recording_dialog_title
        AnalysisReportDialog.RE_WRITE_SCRIPT -> R.string.feature_report_impl_re_write_script_dialog_title
        AnalysisReportDialog.DELETE_REPORT -> R.string.feature_report_impl_delete_report_dialog_title
    }

private val AnalysisReportDialog.descriptionResId: Int
    @StringRes get() = when (this) {
        AnalysisReportDialog.RE_RECORDING -> R.string.feature_report_impl_re_recording_dialog_description
        AnalysisReportDialog.RE_WRITE_SCRIPT -> R.string.feature_report_impl_re_write_script_dialog_description
        AnalysisReportDialog.DELETE_REPORT -> R.string.feature_report_impl_delete_report_dialog_description
    }

private val AnalysisReportDialog.actionTextResId: Int
    @StringRes get() = when (this) {
        AnalysisReportDialog.DELETE_REPORT -> R.string.feature_report_impl_dialog_delete_action

        AnalysisReportDialog.RE_RECORDING,
        AnalysisReportDialog.RE_WRITE_SCRIPT,
        -> R.string.feature_report_impl_dialog_confirm_action
    }

private val AnalysisReportDialog.actionType: PrezelDialogScope.ActionType
    get() = when (this) {
        AnalysisReportDialog.DELETE_REPORT,
        -> PrezelDialogScope.ActionType.BAD

        AnalysisReportDialog.RE_RECORDING,
        AnalysisReportDialog.RE_WRITE_SCRIPT,
        -> PrezelDialogScope.ActionType.GOOD
    }

@BasicPreview
@Composable
private fun ReRecordingReportDialogPreview() {
    PrezelTheme {
        Box(modifier = Modifier.fillMaxSize())
        ReportDialog(
            type = AnalysisReportDialog.RE_RECORDING,
            onDismissClick = {},
            onConformClick = {},
        )
    }
}

@BasicPreview
@Composable
private fun ReWriteScriptReportDialogPreview() {
    PrezelTheme {
        Box(modifier = Modifier.fillMaxSize())
        ReportDialog(
            type = AnalysisReportDialog.RE_WRITE_SCRIPT,
            onDismissClick = {},
            onConformClick = {},
        )
    }
}

@BasicPreview
@Composable
private fun DeleteReportReportDialogPreview() {
    PrezelTheme {
        Box(modifier = Modifier.fillMaxSize())
        ReportDialog(
            type = AnalysisReportDialog.DELETE_REPORT,
            onDismissClick = {},
            onConformClick = {},
        )
    }
}
