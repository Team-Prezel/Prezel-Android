package com.team.prezel.feature.analysis.impl.script

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.team.prezel.core.designsystem.component.feedback.dialog.PrezelDialog
import com.team.prezel.core.designsystem.component.feedback.dialog.PrezelDialogScope.ActionType
import com.team.prezel.core.designsystem.component.navigations.PrezelTabs
import com.team.prezel.core.designsystem.component.textfield.PrezelTextArea
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.component.FileUploader
import com.team.prezel.core.ui.component.FileUploaderState
import com.team.prezel.core.ui.component.StatusView
import com.team.prezel.feature.analysis.impl.R
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
        isHiddenOptions = uiState.isScriptInputOptionsHidden,
        onSelectInputType = onSelectInputType,
        onClearPendingScriptFile = { pendingScriptFileUri = null },
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
    isHiddenOptions: Boolean,
    onSelectInputType: (ScriptInputType) -> Unit,
    onClearPendingScriptFile: () -> Unit,
    onScriptChange: (String) -> Unit,
    onScriptFileUploadClick: () -> Unit,
    onScriptFileClear: () -> Unit,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit,
) {
    var pendingInputTypeChange by remember { mutableStateOf<ScriptInputType?>(null) }

    pendingInputTypeChange?.let { inputType ->
        ScriptInputTypeChangeDialog(
            targetInputType = inputType,
            onDismiss = { pendingInputTypeChange = null },
            onConfirm = {
                onClearPendingScriptFile()
                onSelectInputType(inputType)
                pendingInputTypeChange = null
            },
        )
    }

    AnalysisStepLayout(
        title = stringResource(R.string.feature_analysis_impl_script_title),
        progress = progress,
        buttonText = stringResource(R.string.feature_analysis_impl_next),
        buttonEnabled = buttonEnabled,
        onButtonClick = onNext,
        onBack = onBack,
        isHiddenOptions = isHiddenOptions,
        trailingText = stringResource(R.string.feature_analysis_impl_skip),
        onTrailingTextClick = onSkip,
        contentScrollable = false,
    ) {
        AnalysisStepTitle(
            title = stringResource(R.string.feature_analysis_impl_script_headline),
            description = stringResource(R.string.feature_analysis_impl_script_description),
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        ScriptInputContent(
            form = form,
            pendingScriptFileUri = pendingScriptFileUri,
            uploadProgress = uploadProgress,
            onSelectInputType = onSelectInputType,
            onRequestInputTypeChange = { inputType ->
                pendingInputTypeChange = inputType
            },
            onScriptChange = onScriptChange,
            onScriptFileUploadClick = onScriptFileUploadClick,
            onScriptFileClear = onScriptFileClear,
        )
    }
}

@Composable
private fun ColumnScope.ScriptInputContent(
    form: AnalysisForm,
    pendingScriptFileUri: String?,
    uploadProgress: Float,
    onSelectInputType: (ScriptInputType) -> Unit,
    onRequestInputTypeChange: (ScriptInputType) -> Unit,
    onScriptChange: (String) -> Unit,
    onScriptFileUploadClick: () -> Unit,
    onScriptFileClear: () -> Unit,
) {
    ScriptInputTabs(
        selectedType = form.scriptInputType,
        onSelect = { inputType ->
            if (inputType == form.scriptInputType) return@ScriptInputTabs

            if (form.hasScriptInputHistory || pendingScriptFileUri != null) {
                onRequestInputTypeChange(inputType)
            } else {
                onSelectInputType(inputType)
            }
        },
    )

    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))

    when (form.scriptInputType) {
        ScriptInputType.FILE_UPLOAD -> ScriptUploadCard(
            fileUri = pendingScriptFileUri ?: form.scriptFileUri,
            uploadProgress = if (pendingScriptFileUri != null) uploadProgress else null,
            onClick = onScriptFileUploadClick,
            onClear = onScriptFileClear,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )

        ScriptInputType.DIRECT_INPUT -> DirectScriptInput(
            script = form.script,
            onScriptChange = onScriptChange,
        )
    }
}

@Composable
private fun ColumnScope.DirectScriptInput(
    script: String,
    onScriptChange: (String) -> Unit,
) {
    PrezelTextArea(
        value = script,
        onValueChange = onScriptChange,
        placeholder = stringResource(R.string.feature_analysis_impl_script_placeholder),
        maxLength = SCRIPT_MAX_LENGTH,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        fillContainerHeight = true,
    )
    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
}

@Composable
private fun ScriptInputTypeChangeDialog(
    targetInputType: ScriptInputType,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    PrezelDialog(
        title = stringResource(targetInputType.changeDialogTitleResId),
        description = stringResource(targetInputType.changeDialogDescriptionResId),
        onDismiss = onDismiss,
    ) {
        Action(label = stringResource(R.string.feature_analysis_impl_script_input_type_change_dialog_cancel)) {
            onDismiss()
        }
        Action(
            label = stringResource(targetInputType.changeDialogConfirmResId),
            type = ActionType.BAD,
        ) {
            onConfirm()
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
        onClickTab = { page ->
            onSelect(page.toScriptInputType())
        },
    )
}

private val AnalysisForm.hasScriptInputHistory: Boolean
    get() = script.isNotBlank() || scriptFileUri != null

private val ScriptInputType.changeDialogTitleResId: Int
    get() = when (this) {
        ScriptInputType.FILE_UPLOAD -> R.string.feature_analysis_impl_script_upload_change_dialog_title
        ScriptInputType.DIRECT_INPUT -> R.string.feature_analysis_impl_script_direct_change_dialog_title
    }

private val ScriptInputType.changeDialogDescriptionResId: Int
    get() = when (this) {
        ScriptInputType.FILE_UPLOAD -> R.string.feature_analysis_impl_script_upload_change_dialog_description
        ScriptInputType.DIRECT_INPUT -> R.string.feature_analysis_impl_script_direct_change_dialog_description
    }

private val ScriptInputType.changeDialogConfirmResId: Int
    get() = when (this) {
        ScriptInputType.FILE_UPLOAD -> R.string.feature_analysis_impl_script_upload_change_dialog_confirm
        ScriptInputType.DIRECT_INPUT -> R.string.feature_analysis_impl_script_direct_change_dialog_confirm
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
    modifier: Modifier = Modifier,
) {
    if (fileUri == null) {
        EmptyScriptUploadContent(
            onClick = onClick,
            modifier = modifier,
        )
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            UploadedScriptFileCard(
                fileUri = fileUri,
                uploadProgress = uploadProgress,
                onClear = onClear,
            )
        }
    }
}

@Composable
private fun EmptyScriptUploadContent(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatusView(
        title = stringResource(R.string.feature_analysis_impl_script_file_placeholder),
        description = stringResource(R.string.feature_analysis_impl_script_file_format),
        modifier = modifier,
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
                size = ButtonSize.SMALL,
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
            isHiddenOptions = false,
            onSelectInputType = {},
            onClearPendingScriptFile = {},
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

@BasicPreview
@Composable
private fun ScriptInputLongDirectScreenPreview() {
    PrezelTheme {
        ScriptInputScreen(
            uiState = AnalysisFlowUiState(
                step = AnalysisFlowStep.SCRIPT_INPUT,
                form = AnalysisForm(
                    scriptInputType = ScriptInputType.DIRECT_INPUT,
                    script =
                        """
                        안녕하세요 자신의 말로 자신있게 세상을 설득할 수 있는 날을 기다리는 팀 손가락입니다.
                        저희 팀은 고승환, 박하영, 조민경, 최수빈, 한효주 총 5명으로 구성되어 있으며,
                        다음과 같은 목차로 발표 진행하겠습니다.
                        한 번쯤 발표하면서 긴장하신 경험 있으시죠. 오늘도 다들 긴장되는 마음으로 오셨을 것 같습니다.
                        저희는 학교에서의 간단한 자기소개부터 회사의 성과보고까지 정말 다양하게,
                        그리고 정말 자주 발표를 경험합니다. 하지만 많은 발표를 해왔음에도 불구하고
                        발표를 생각했을 때 긴장하게 되는데요. 이처럼 발표를 앞둔 상황에서 경험하는
                        두려움과 심리적 압박감을 발표 불안이라 합니다.
                        면접에서도, 학교에서도, 발표 능력을 기본 역량처럼 여기는 사회 분위기로 인해
                        이런 불안이 더해지고자 때문이었습니다. 가장 발표를 자주 경험하는 직장인을 예시로 들었을 때,
                        실수에 대한 두려움을 발표 불안의 주된 원인으로 꼽았습니다.
                        즉, 발표와 가까운 환경의 사람들조차 실수가 두려워 발표에 어려움을 겪고 있다는 건데요.
                        이때 사람들은 클래스 수강, 집단 상담 등 발표 불안을 이겨내기 위해 다양한 시도를 하고 있었습니다.
                        특히 발표 코칭 학원을 등록하며 적극적인 대처를 취하는 사람들까지는 증가하고 있습니다.
                        그러나 대학생과 사회 초년생의 평균 수입과 비교했을 때 비싼 비용과 시간적 여유가 없어
                        지속적으로 수강하기 어렵다는 문제점이 있었습니다.
                        비용과 시간, 이런 고질적인 문제를 해결할 방법은 없을까요?
                        """.trimIndent(),
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
