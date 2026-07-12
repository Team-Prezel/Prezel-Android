package com.team.prezel.feature.analysis.impl.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.analysis.impl.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AnalysisStepLayout(
    title: String,
    progress: Float,
    buttonText: String,
    buttonEnabled: Boolean,
    onButtonClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    trailingText: String? = null,
    onTrailingTextClick: (() -> Unit)? = null,
    subButtonText: String? = null,
    onSubButtonClick: (() -> Unit)? = null,
    contentScrollable: Boolean = true,
    isHiddenOptions: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val contentScrollState = rememberScrollState()
    val showButtonAreaDivider by remember {
        derivedStateOf { contentScrollable && contentScrollState.maxValue > 0 }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrezelTheme.colors.bgRegular),
    ) {
        PrezelTopAppBar(
            title = { Text(text = title) },
            leadingIcon = {
                if (isHiddenOptions) return@PrezelTopAppBar
                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(PrezelIcons.ArrowLeft),
                        contentDescription = stringResource(R.string.feature_analysis_impl_back),
                    )
                }
            },
            trailingIcons = {
                if (isHiddenOptions) {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(PrezelIcons.Cancel),
                            contentDescription = null,
                        )
                    }
                    return@PrezelTopAppBar
                }
                AnalysisStepTrailingText(
                    text = trailingText,
                    onClick = onTrailingTextClick,
                )
            },
        )

        ProgressBar(progress = progress)

        AnalysisStepContent(
            scrollState = contentScrollState,
            scrollable = contentScrollable,
            content = content,
        )

        AnalysisStepButtonArea(
            buttonText = buttonText,
            buttonEnabled = buttonEnabled,
            showDivider = showButtonAreaDivider,
            onButtonClick = onButtonClick,
            subButtonText = subButtonText,
            onSubButtonClick = onSubButtonClick,
        )
    }
}

@Composable
private fun AnalysisStepTrailingText(
    text: String?,
    onClick: (() -> Unit)?,
) {
    if (text == null || onClick == null) return

    PrezelTouchArea(
        onClick = onClick,
        extraTouchPadding = PaddingValues(PrezelTheme.spacing.V8),
    ) {
        Text(
            text = text,
            color = PrezelTheme.colors.textMedium,
            style = PrezelTheme.typography.body3Medium,
        )
    }
    Spacer(modifier = Modifier.width(PrezelTheme.spacing.V8))
}

@Composable
private fun ColumnScope.AnalysisStepContent(
    scrollState: ScrollState,
    scrollable: Boolean,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .then(if (scrollable) Modifier.verticalScroll(scrollState) else Modifier)
            .padding(horizontal = PrezelTheme.spacing.V20)
            .padding(top = PrezelTheme.spacing.V40),
    ) {
        content()
    }
}

@Composable
private fun AnalysisStepButtonArea(
    buttonText: String,
    buttonEnabled: Boolean,
    showDivider: Boolean,
    onButtonClick: () -> Unit,
    subButtonText: String?,
    onSubButtonClick: (() -> Unit)?,
) {
    PrezelButtonArea(
        modifier = Modifier.background(PrezelTheme.colors.bgRegular),
        showBackground = showDivider,
        mainButton = { modifier ->
            PrezelButton(
                modifier = modifier,
                text = buttonText,
                enabled = buttonEnabled,
                onClick = onButtonClick,
                type = ButtonType.FILLED,
                hierarchy = ButtonHierarchy.PRIMARY,
            )
        },
        subButton = if (subButtonText != null && onSubButtonClick != null) {
            { modifier ->
                PrezelButton(
                    modifier = modifier,
                    text = subButtonText,
                    onClick = onSubButtonClick,
                    type = ButtonType.FILLED,
                    hierarchy = ButtonHierarchy.SECONDARY,
                )
            }
        } else {
            null
        },
    )
}

@Composable
internal fun AnalysisStepTitle(
    title: String,
    description: String,
) {
    Text(
        text = title,
        color = PrezelTheme.colors.textLarge,
        style = PrezelTheme.typography.title2Bold,
    )
    Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))
    Text(
        text = description,
        color = PrezelTheme.colors.textRegular,
        style = PrezelTheme.typography.body2Regular,
    )
}

@Composable
private fun ProgressBar(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(PrezelTheme.colors.bgLarge),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(4.dp)
                .background(
                    color = PrezelTheme.colors.interactiveRegular,
                    shape = RoundedCornerShape(
                        topEnd = PrezelTheme.radius.V1000,
                        bottomEnd = PrezelTheme.radius.V1000,
                    ),
                ),
        )
    }
}
