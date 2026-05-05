package com.team.prezel.feature.analysis.impl

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea
import com.team.prezel.core.designsystem.component.navigations.PrezelTabSize
import com.team.prezel.core.designsystem.component.navigations.PrezelTabs
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.StatusView
import com.team.prezel.feature.analysis.impl.component.AnalysisStepLayout
import com.team.prezel.feature.analysis.impl.component.AnalysisStepTitle
import com.team.prezel.feature.analysis.impl.component.toFileName
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowStep
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiState
import com.team.prezel.feature.analysis.impl.contract.AnalysisForm
import kotlinx.collections.immutable.persistentListOf

private const val AUDIO_FILE_MIME_TYPE = "audio/*"
private const val AUDIO_PREVIEW_FILE_URI = "content://prezel/sample.wav"
private const val AUDIO_UPLOAD_TAB_COUNT = 1
private const val UPLOADED_AUDIO_PROGRESS = 0f

@Composable
internal fun AudioUploadScreen(
    uiState: AnalysisFlowUiState,
    onAudioFileSelected: (String?) -> Unit,
    onAnalyze: () -> Unit,
    onBack: () -> Unit,
) {
    val audioPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        onAudioFileSelected(uri?.toString())
    }

    AudioUploadScreen(
        form = uiState.form,
        progress = uiState.progress,
        buttonEnabled = uiState.canMoveNext,
        onAudioFileUploadClick = { audioPicker.launch(AUDIO_FILE_MIME_TYPE) },
        onAudioFileClear = { onAudioFileSelected(null) },
        onAnalyze = onAnalyze,
        onBack = onBack,
    )
}

@Composable
private fun AudioUploadScreen(
    form: AnalysisForm,
    progress: Float,
    buttonEnabled: Boolean,
    onAudioFileUploadClick: () -> Unit,
    onAudioFileClear: () -> Unit,
    onAnalyze: () -> Unit,
    onBack: () -> Unit,
) {
    val pagerState = rememberPagerState { AUDIO_UPLOAD_TAB_COUNT }
    val tabs = persistentListOf(stringResource(R.string.feature_analysis_impl_upload_title))

    AnalysisStepLayout(
        title = stringResource(R.string.feature_analysis_impl_audio_title),
        progress = progress,
        buttonText = stringResource(R.string.feature_analysis_impl_analyze),
        buttonEnabled = buttonEnabled,
        onButtonClick = onAnalyze,
        onBack = onBack,
    ) {
        AnalysisStepTitle(
            title = stringResource(R.string.feature_analysis_impl_audio_headline),
            description = stringResource(R.string.feature_analysis_impl_audio_description),
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        PrezelTabs(
            tabs = tabs,
            pagerState = pagerState,
            size = PrezelTabSize.MEDIUM,
            onClickTab = {},
        )

        AudioUploadContent(
            fileUri = form.audioFileUri,
            onUploadClick = onAudioFileUploadClick,
            onClear = onAudioFileClear,
        )
    }
}

@Composable
private fun AudioUploadContent(
    fileUri: String?,
    onUploadClick: () -> Unit,
    onClear: () -> Unit,
) {
    if (fileUri == null) {
        AudioUploadEmptyContent(onUploadClick = onUploadClick)
    } else {
        val context = LocalContext.current

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
        UploadedAudioFileCard(
            fileName = remember(context, fileUri) { fileUri.toFileName(context) },
            onClear = onClear,
        )
    }
}

@Composable
private fun AudioUploadEmptyContent(onUploadClick: () -> Unit) {
    StatusView(
        title = stringResource(R.string.feature_analysis_impl_audio_file_placeholder),
        description = stringResource(R.string.feature_analysis_impl_audio_file_format),
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        visual = {
            Image(
                painter = painterResource(R.drawable.feature_analysis_impl_no_voice),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
            )
        },
        action = {
            PrezelButton(
                text = stringResource(R.string.feature_analysis_impl_audio_upload_button),
                iconResId = PrezelIcons.Plus,
                type = ButtonType.OUTLINED,
                size = ButtonSize.REGULAR,
                isRounded = true,
                onClick = onUploadClick,
            )
        },
    )
}

@Composable
private fun UploadedAudioFileCard(
    fileName: String,
    onClear: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = PrezelTheme.stroke.V1,
                color = PrezelTheme.colors.borderSmall,
                shape = PrezelTheme.shapes.V8,
            ).padding(
                horizontal = PrezelTheme.spacing.V16,
                vertical = PrezelTheme.spacing.V16,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UploadedAudioFileInfo(
            fileName = fileName,
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.size(PrezelTheme.spacing.V12))

        PrezelTouchArea(
            extraTouchPadding = PaddingValues(PrezelTheme.spacing.V8),
            onClick = onClear,
        ) {
            Icon(
                painter = painterResource(PrezelIcons.CancelCircleFilled),
                contentDescription = stringResource(R.string.feature_analysis_impl_audio_file_remove),
                modifier = Modifier.size(24.dp),
                tint = PrezelTheme.colors.iconRegular,
            )
        }
    }
}

@Composable
private fun UploadedAudioFileInfo(
    fileName: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        AudioFileTitleRow(fileName = fileName)

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))

        AudioFileProgressRow()
    }
}

@Composable
private fun AudioFileTitleRow(fileName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(PrezelIcons.Play),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = PrezelTheme.colors.iconRegular,
        )
        Spacer(modifier = Modifier.size(PrezelTheme.spacing.V8))
        Text(
            text = fileName,
            modifier = Modifier.weight(1f),
            color = PrezelTheme.colors.textMedium,
            style = PrezelTheme.typography.body3Medium,
        )
    }
}

@Composable
private fun AudioFileProgressRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.feature_analysis_impl_audio_file_duration_placeholder),
            color = PrezelTheme.colors.textSmall,
            style = PrezelTheme.typography.caption2Regular,
        )
        Spacer(modifier = Modifier.size(PrezelTheme.spacing.V8))
        AudioProgressTrack(
            progress = UPLOADED_AUDIO_PROGRESS,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun AudioProgressTrack(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.height(16.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape)
                .background(PrezelTheme.colors.bgDisabled),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(6.dp)
                .clip(CircleShape)
                .background(PrezelTheme.colors.interactiveRegular),
        )
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(PrezelTheme.colors.interactiveRegular),
        )
    }
}

@BasicPreview
@Composable
private fun AudioUploadScreenPreview() {
    PrezelTheme {
        AudioUploadScreen(
            uiState = AnalysisFlowUiState(step = AnalysisFlowStep.AUDIO_UPLOAD),
            onAudioFileSelected = {},
            onAnalyze = {},
            onBack = {},
        )
    }
}

@BasicPreview
@Composable
private fun AudioUploadScreenSelectedPreview() {
    PrezelTheme {
        AudioUploadScreen(
            uiState = AnalysisFlowUiState(
                step = AnalysisFlowStep.AUDIO_UPLOAD,
                form = AnalysisForm(audioFileUri = AUDIO_PREVIEW_FILE_URI),
            ),
            onAudioFileSelected = {},
            onAnalyze = {},
            onBack = {},
        )
    }
}
