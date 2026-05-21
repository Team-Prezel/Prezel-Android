package com.team.prezel.feature.analysis.impl

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.navigations.PrezelTabSize
import com.team.prezel.core.designsystem.component.navigations.PrezelTabs
import com.team.prezel.core.designsystem.component.textfield.PrezelTextArea
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.FileUploader
import com.team.prezel.core.ui.component.FileUploaderState
import com.team.prezel.core.ui.component.StatusView
import com.team.prezel.feature.analysis.impl.component.AnalysisStepLayout
import com.team.prezel.feature.analysis.impl.component.AnalysisStepTitle
import com.team.prezel.feature.analysis.impl.component.toFileName
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowStep
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiState
import com.team.prezel.feature.analysis.impl.contract.AnalysisForm
import com.team.prezel.feature.analysis.impl.contract.ScriptInputType
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay

private const val SCRIPT_MAX_LENGTH = 5_000
private const val SCRIPT_UPLOAD_PROGRESS_DURATION_MILLIS = 800
private val SCRIPT_FILE_MIME_TYPES = arrayOf("text/plain")
private const val SCRIPT_INPUT_TAB_COUNT = 2

@Composable
internal fun ScriptInputScreen(
    uiState: AnalysisFlowUiState,
    onSelectInputType: (ScriptInputType) -> Unit,
    onScriptChange: (String) -> Unit,
    onScriptFileSelected: (String?) -> Unit,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit,
) {
    var pendingScriptFileUri by remember { mutableStateOf<String?>(null) }
    val uploadProgress by animateFloatAsState(
        targetValue = if (pendingScriptFileUri != null) 1f else 0f,
        animationSpec = tween(
            durationMillis = SCRIPT_UPLOAD_PROGRESS_DURATION_MILLIS,
            easing = LinearEasing,
        ),
        label = "script-upload-progress",
    )

    val scriptPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.toString()?.let { fileUri ->
            pendingScriptFileUri = fileUri
        }
    }

    LaunchedEffect(pendingScriptFileUri) {
        val fileUri = pendingScriptFileUri ?: return@LaunchedEffect
        delay(SCRIPT_UPLOAD_PROGRESS_DURATION_MILLIS.toLong())
        onScriptFileSelected(fileUri)
    }

    LaunchedEffect(uiState.form.scriptFileUri, pendingScriptFileUri) {
        if (pendingScriptFileUri != null && uiState.form.scriptFileUri == pendingScriptFileUri) {
            pendingScriptFileUri = null
        }
    }

    ScriptInputScreen(
        form = uiState.form,
        pendingScriptFileUri = pendingScriptFileUri,
        uploadProgress = uploadProgress,
        progress = uiState.progress,
        buttonEnabled = uiState.canMoveNext,
        onSelectInputType = onSelectInputType,
        onScriptChange = onScriptChange,
        onScriptFileUploadClick = { scriptPicker.launch(SCRIPT_FILE_MIME_TYPES) },
        onScriptFileClear = {
            if (pendingScriptFileUri != null) {
                pendingScriptFileUri = null
            } else {
                onScriptFileSelected(null)
            }
        },
        onNext = onNext,
        onSkip = onSkip,
        onBack = onBack,
    )
}

@Composable
private fun ScriptInputScreen(
    form: AnalysisForm,
    pendingScriptFileUri: String?,
    uploadProgress: Float,
    progress: Float,
    buttonEnabled: Boolean,
    onSelectInputType: (ScriptInputType) -> Unit,
    onScriptChange: (String) -> Unit,
    onScriptFileUploadClick: () -> Unit,
    onScriptFileClear: () -> Unit,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit,
) {
    AnalysisStepLayout(
        title = stringResource(R.string.feature_analysis_impl_script_title),
        progress = progress,
        buttonText = stringResource(R.string.feature_analysis_impl_next),
        buttonEnabled = buttonEnabled,
        onButtonClick = onNext,
        onBack = onBack,
        trailingText = stringResource(R.string.feature_analysis_impl_skip),
        onTrailingTextClick = onSkip,
    ) {
        AnalysisStepTitle(
            title = stringResource(R.string.feature_analysis_impl_script_headline),
            description = stringResource(R.string.feature_analysis_impl_script_description),
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        ScriptInputTabs(
            selectedType = form.scriptInputType,
            onSelect = onSelectInputType,
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))

        when (form.scriptInputType) {
            ScriptInputType.FILE_UPLOAD -> ScriptUploadCard(
                fileUri = pendingScriptFileUri ?: form.scriptFileUri,
                uploadProgress = if (pendingScriptFileUri != null) uploadProgress else null,
                onClick = onScriptFileUploadClick,
                onClear = onScriptFileClear,
            )

            ScriptInputType.DIRECT_INPUT -> {
                PrezelTextArea(
                    value = form.script,
                    onValueChange = onScriptChange,
                    placeholder = stringResource(R.string.feature_analysis_impl_script_placeholder),
                    maxLength = SCRIPT_MAX_LENGTH,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun ScriptInputTabs(
    selectedType: ScriptInputType,
    onSelect: (ScriptInputType) -> Unit,
) {
    val selectedPage = selectedType.toTabPage()
    val pagerState = rememberPagerState(initialPage = selectedPage) { SCRIPT_INPUT_TAB_COUNT }
    val tabs = persistentListOf(
        stringResource(R.string.feature_analysis_impl_upload_title),
        stringResource(R.string.feature_analysis_impl_direct_input_title),
    )

    LaunchedEffect(selectedPage) {
        if (pagerState.currentPage != selectedPage) {
            pagerState.requestScrollToPage(selectedPage)
        }
    }

    PrezelTabs(
        tabs = tabs,
        pagerState = pagerState,
        size = PrezelTabSize.MEDIUM,
        onClickTab = { page ->
            pagerState.requestScrollToPage(page)
            onSelect(page.toScriptInputType())
        },
    )
}

private fun ScriptInputType.toTabPage(): Int =
    when (this) {
        ScriptInputType.FILE_UPLOAD -> 0
        ScriptInputType.DIRECT_INPUT -> 1
    }

private fun Int.toScriptInputType(): ScriptInputType =
    when (this) {
        0 -> ScriptInputType.FILE_UPLOAD
        else -> ScriptInputType.DIRECT_INPUT
    }

@Composable
private fun ScriptUploadCard(
    fileUri: String?,
    uploadProgress: Float?,
    onClick: () -> Unit,
    onClear: () -> Unit,
) {
    if (fileUri == null) {
        EmptyScriptUploadContent(onClick = onClick)
    } else {
        UploadedScriptFileCard(
            fileUri = fileUri,
            uploadProgress = uploadProgress,
            onClear = onClear,
        )
    }
}

@Composable
private fun EmptyScriptUploadContent(onClick: () -> Unit) {
    StatusView(
        title = stringResource(R.string.feature_analysis_impl_script_file_placeholder),
        description = stringResource(R.string.feature_analysis_impl_script_file_format),
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp),
        visual = {
            Image(
                painter = painterResource(R.drawable.feature_analysis_impl_no_script),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
            )
        },
        action = {
            PrezelButton(
                text = stringResource(R.string.feature_analysis_impl_script_upload_button),
                iconResId = PrezelIcons.Plus,
                type = ButtonType.OUTLINED,
                size = ButtonSize.REGULAR,
                isRounded = true,
                onClick = onClick,
            )
        },
    )
}

@Composable
private fun UploadedScriptFileCard(
    fileUri: String,
    uploadProgress: Float?,
    onClear: () -> Unit,
) {
    val context = LocalContext.current
    val fileName = remember(context, fileUri) { fileUri.toFileName(context) }

    FileUploader(
        fileName = fileName,
        state = if (uploadProgress == null) {
            FileUploaderState.Script.Uploaded
        } else {
            FileUploaderState.Script.Loading
        },
        progress = uploadProgress ?: 0f,
        onCancelClick = onClear,
    )
}

@BasicPreview
@Composable
private fun ScriptInputUploadScreenPreview() {
    PrezelTheme {
        ScriptInputScreen(
            uiState = AnalysisFlowUiState(step = AnalysisFlowStep.SCRIPT_INPUT),
            onSelectInputType = {},
            onScriptChange = {},
            onScriptFileSelected = {},
            onNext = {},
            onSkip = {},
            onBack = {},
        )
    }
}

@BasicPreview
@Composable
private fun ScriptInputUploadProgressScreenPreview() {
    PrezelTheme {
        ScriptInputScreen(
            form = AnalysisForm(),
            pendingScriptFileUri = "content://prezel/25-2 컨셉발표회 대본.txt",
            uploadProgress = 0.5f,
            progress = AnalysisFlowUiState(step = AnalysisFlowStep.SCRIPT_INPUT).progress,
            buttonEnabled = false,
            onSelectInputType = {},
            onScriptChange = {},
            onScriptFileUploadClick = {},
            onScriptFileClear = {},
            onNext = {},
            onSkip = {},
            onBack = {},
        )
    }
}

@BasicPreview
@Composable
private fun ScriptInputUploadedScreenPreview() {
    PrezelTheme {
        ScriptInputScreen(
            uiState = AnalysisFlowUiState(
                step = AnalysisFlowStep.SCRIPT_INPUT,
                form = AnalysisForm(scriptFileUri = "content://prezel/25-2 컨셉발표회 대본.txt"),
            ),
            onSelectInputType = {},
            onScriptChange = {},
            onScriptFileSelected = {},
            onNext = {},
            onSkip = {},
            onBack = {},
        )
    }
}

@BasicPreview
@Composable
private fun ScriptInputDirectScreenPreview() {
    PrezelTheme {
        ScriptInputScreen(
            uiState = AnalysisFlowUiState(
                step = AnalysisFlowStep.SCRIPT_INPUT,
                form = AnalysisForm(
                    scriptInputType = ScriptInputType.DIRECT_INPUT,
                    script = stringResource(R.string.feature_analysis_impl_script_placeholder),
                ),
            ),
            onSelectInputType = {},
            onScriptChange = {},
            onScriptFileSelected = {},
            onNext = {},
            onSkip = {},
            onBack = {},
        )
    }
}
