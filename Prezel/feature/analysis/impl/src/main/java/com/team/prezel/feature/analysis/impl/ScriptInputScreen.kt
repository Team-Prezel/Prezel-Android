package com.team.prezel.feature.analysis.impl

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea
import com.team.prezel.core.designsystem.component.list.PrezelList
import com.team.prezel.core.designsystem.component.list.PrezelListSize
import com.team.prezel.core.designsystem.component.navigations.PrezelTabSize
import com.team.prezel.core.designsystem.component.navigations.PrezelTabs
import com.team.prezel.core.designsystem.component.textfield.PrezelTextArea
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.analysis.impl.component.AnalysisStepLayout
import com.team.prezel.feature.analysis.impl.component.AnalysisStepTitle
import com.team.prezel.feature.analysis.impl.component.toFileName
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowStep
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiState
import com.team.prezel.feature.analysis.impl.contract.AnalysisForm
import com.team.prezel.feature.analysis.impl.contract.ScriptInputType
import kotlinx.collections.immutable.persistentListOf

private const val SCRIPT_MAX_LENGTH = 5_000
private const val SCRIPT_FILE_MIME_TYPE = "text/*"
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
    val scriptPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        onScriptFileSelected(uri?.toString())
    }

    ScriptInputScreen(
        form = uiState.form,
        progress = uiState.progress,
        buttonEnabled = uiState.canMoveNext,
        onSelectInputType = onSelectInputType,
        onScriptChange = onScriptChange,
        onScriptFileUploadClick = { scriptPicker.launch(SCRIPT_FILE_MIME_TYPE) },
        onScriptFileClear = { onScriptFileSelected(null) },
        onNext = onNext,
        onSkip = onSkip,
        onBack = onBack,
    )
}

@Composable
private fun ScriptInputScreen(
    form: AnalysisForm,
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
                fileUri = form.scriptFileUri,
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
    onClick: () -> Unit,
    onClear: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrezelTheme.colors.bgMedium, PrezelTheme.shapes.V8)
            .padding(all = PrezelTheme.spacing.V20),
    ) {
        if (fileUri != null) {
            UploadedScriptFileContent(
                fileName = fileUri.toFileName(),
                onClear = onClear,
            )
            return@Column
        }

        Text(
            text = stringResource(R.string.feature_analysis_impl_script_file_placeholder),
            color = PrezelTheme.colors.textLarge,
            style = PrezelTheme.typography.body3Bold,
        )
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V4))
        Text(
            text = stringResource(R.string.feature_analysis_impl_script_file_format),
            color = PrezelTheme.colors.textSmall,
            style = PrezelTheme.typography.caption2Medium,
        )
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V20))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            PrezelButton(
                text = stringResource(R.string.feature_analysis_impl_script_upload_button),
                iconResId = PrezelIcons.Plus,
                type = ButtonType.OUTLINED,
                size = ButtonSize.REGULAR,
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun UploadedScriptFileContent(
    fileName: String,
    onClear: () -> Unit,
) {
    Text(
        text = stringResource(R.string.feature_analysis_impl_script_file_placeholder),
        color = PrezelTheme.colors.textLarge,
        style = PrezelTheme.typography.body3Bold,
    )
    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
    CompositionLocalProvider(LocalContentColor provides PrezelTheme.colors.textLarge) {
        PrezelList(
            title = fileName,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = PrezelTheme.stroke.V1,
                    color = PrezelTheme.colors.borderSmall,
                    shape = PrezelTheme.shapes.V8,
                ),
            size = PrezelListSize.SMALL,
            trailingContent = {
                PrezelTouchArea(
                    extraTouchPadding = PaddingValues(PrezelTheme.spacing.V8),
                    onClick = onClear,
                ) {
                    Icon(
                        painter = painterResource(PrezelIcons.CancelCircleFilled),
                        contentDescription = stringResource(R.string.feature_analysis_impl_script_file_remove),
                        modifier = Modifier.size(24.dp),
                        tint = PrezelTheme.colors.iconRegular,
                    )
                }
            },
        )
    }
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
