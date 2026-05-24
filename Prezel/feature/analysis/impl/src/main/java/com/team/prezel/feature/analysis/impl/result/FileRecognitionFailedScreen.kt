package com.team.prezel.feature.analysis.impl.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.StatusView
import com.team.prezel.feature.analysis.impl.R

@Composable
internal fun FileRecognitionFailedScreen(onRetry: () -> Unit) {
    FileRecognitionFailedStatusView(
        title = stringResource(R.string.feature_analysis_impl_file_recognition_failed_title),
        description = stringResource(R.string.feature_analysis_impl_file_recognition_failed_description),
        onRetry = onRetry,
    )
}

@Composable
internal fun ScriptFileRecognitionFailedScreen(onRetry: () -> Unit) {
    FileRecognitionFailedStatusView(
        title = stringResource(R.string.feature_analysis_impl_script_file_recognition_failed_title),
        description = stringResource(R.string.feature_analysis_impl_script_file_recognition_failed_description),
        onRetry = onRetry,
    )
}

@Composable
private fun FileRecognitionFailedStatusView(
    title: String,
    description: String,
    onRetry: () -> Unit,
) {
    StatusView(
        title = title,
        description = description,
        modifier = Modifier.fillMaxSize(),
        visual = {
            Image(
                painter = painterResource(R.drawable.feature_analysis_impl_error_voice),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
            )
        },
        action = {
            PrezelButton(
                text = stringResource(R.string.feature_analysis_impl_retry),
                iconResId = PrezelIcons.Reset,
                type = ButtonType.FILLED,
                size = ButtonSize.SMALL,
                hierarchy = ButtonHierarchy.SECONDARY,
                isRounded = true,
                onClick = onRetry,
            )
        },
    )
}

@BasicPreview
@Composable
private fun FileRecognitionFailedScreenPreview() {
    PrezelTheme {
        FileRecognitionFailedScreen(onRetry = {})
    }
}

@BasicPreview
@Composable
private fun ScriptFileRecognitionFailedScreenPreview() {
    PrezelTheme {
        ScriptFileRecognitionFailedScreen(onRetry = {})
    }
}
