package com.team.prezel.feature.analysis.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.PrezelAccordion
import com.team.prezel.core.designsystem.component.base.PrezelTouchArea
import com.team.prezel.core.designsystem.component.chip.chip.ChipSize
import com.team.prezel.core.designsystem.component.chip.chip.ChipState
import com.team.prezel.core.designsystem.component.chip.chip.ChipType
import com.team.prezel.core.designsystem.component.chip.chip.PrezelChip
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.presentation.Audience
import com.team.prezel.core.model.presentation.Category
import com.team.prezel.core.model.presentation.Purpose
import com.team.prezel.core.model.presentation.Style
import com.team.prezel.feature.analysis.impl.component.AnalysisStepLayout
import com.team.prezel.feature.analysis.impl.component.AnalysisStepTitle
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowStep
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiState
import com.team.prezel.feature.analysis.impl.contract.AnalysisForm
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun PresentationSituationScreen(
    uiState: AnalysisFlowUiState,
    onSelectCategory: (Category) -> Unit,
    onSelectPurpose: (Purpose) -> Unit,
    onSelectStyle: (Style) -> Unit,
    onSelectAudience: (Audience) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    PresentationSituationScreen(
        form = uiState.form,
        progress = uiState.progress,
        buttonEnabled = uiState.canMoveNext,
        onSelectCategory = onSelectCategory,
        onSelectPurpose = onSelectPurpose,
        onSelectStyle = onSelectStyle,
        onSelectAudience = onSelectAudience,
        onNext = onNext,
        onBack = onBack,
    )
}

@Composable
private fun PresentationSituationScreen(
    form: AnalysisForm,
    progress: Float,
    buttonEnabled: Boolean,
    onSelectCategory: (Category) -> Unit,
    onSelectPurpose: (Purpose) -> Unit,
    onSelectStyle: (Style) -> Unit,
    onSelectAudience: (Audience) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    AnalysisStepLayout(
        title = stringResource(R.string.feature_analysis_impl_situation_title),
        progress = progress,
        buttonText = stringResource(R.string.feature_analysis_impl_next),
        buttonEnabled = buttonEnabled,
        onButtonClick = onNext,
        onBack = onBack,
    ) {
        AnalysisStepTitle(
            title = stringResource(R.string.feature_analysis_impl_situation_headline),
            description = stringResource(R.string.feature_analysis_impl_situation_description),
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        SituationAccordions(
            form = form,
            onSelectCategory = onSelectCategory,
            onSelectPurpose = onSelectPurpose,
            onSelectStyle = onSelectStyle,
            onSelectAudience = onSelectAudience,
        )
    }
}

@Composable
private fun SituationAccordions(
    form: AnalysisForm,
    onSelectCategory: (Category) -> Unit,
    onSelectPurpose: (Purpose) -> Unit,
    onSelectStyle: (Style) -> Unit,
    onSelectAudience: (Audience) -> Unit,
) {
    val categoryOptions = categoryOptions()
    val purposeOptions = purposeOptions()
    val styleOptions = styleOptions()
    val audienceOptions = audienceOptions()

    SituationAccordion(
        title = stringResource(R.string.feature_analysis_impl_situation_category_label),
    ) {
        CategoryOptionGrid(
            selectedValue = form.category,
            options = categoryOptions,
            onSelect = onSelectCategory,
        )
    }
    SituationAccordion(
        title = stringResource(R.string.feature_analysis_impl_situation_purpose_label),
    ) {
        ChipOptionsContent(
            options = purposeOptions.toChipContentOptions(form.purpose),
            onSelect = { index -> onSelectPurpose(purposeOptions[index].value) },
        )
    }
    SituationAccordion(
        title = stringResource(R.string.feature_analysis_impl_situation_style_label),
    ) {
        ChipOptionsContent(
            options = styleOptions.toChipContentOptions(form.style),
            onSelect = { index -> onSelectStyle(styleOptions[index].value) },
        )
    }
    SituationAccordion(
        title = stringResource(R.string.feature_analysis_impl_situation_scale_label),
    ) {
        ChipOptionsContent(
            options = audienceOptions.toChipContentOptions(form.audience),
            onSelect = { index -> onSelectAudience(audienceOptions[index].value) },
        )
    }
}

@Composable
private fun SituationAccordion(
    title: String,
    content: @Composable () -> Unit,
) {
    PrezelAccordion(
        title = title,
        showDivider = true,
        content = content,
    )
}

@Composable
private fun CategoryOptionGrid(
    selectedValue: Category?,
    options: ImmutableList<SituationCategoryOption>,
    onSelect: (Category) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16)) {
        options.chunked(2).forEach { rowOptions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
            ) {
                rowOptions.forEach { option ->
                    CategoryOptionCard(
                        option = option,
                        selected = selectedValue == option.value,
                        onClick = { onSelect(option.value) },
                        modifier = Modifier.weight(1f),
                    )
                }
                if (rowOptions.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CategoryOptionCard(
    option: SituationCategoryOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (selected) PrezelTheme.colors.interactiveRegular else PrezelTheme.colors.bgMedium
    val backgroundColor = if (selected) PrezelTheme.colors.interactiveXSmall else PrezelTheme.colors.bgMedium
    val iconColor = if (selected) PrezelTheme.colors.interactiveRegular else PrezelTheme.colors.iconRegular

    PrezelTouchArea(
        onClick = onClick,
        shape = PrezelTheme.shapes.V8,
        modifier = modifier,
        isUseRipple = false,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, PrezelTheme.shapes.V8)
                .border(
                    width = PrezelTheme.stroke.V1,
                    color = borderColor,
                    shape = PrezelTheme.shapes.V8,
                ).padding(PrezelTheme.spacing.V12),
        ) {
            Icon(
                painter = painterResource(option.iconResId),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = iconColor,
            )
            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V4))
            Text(
                text = option.title,
                color = PrezelTheme.colors.textLarge,
                style = PrezelTheme.typography.body2Medium,
            )
            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V4))
            Text(
                text = option.description,
                color = PrezelTheme.colors.textRegular,
                style = PrezelTheme.typography.caption2Regular,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipOptionsContent(
    options: ImmutableList<SituationChipContentOption>,
    onSelect: (Int) -> Unit,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrezelTheme.colors.bgMedium)
            .padding(horizontal = PrezelTheme.spacing.V12, vertical = PrezelTheme.spacing.V14),
        horizontalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V10),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V10),
    ) {
        options.forEachIndexed { index, option ->
            SituationChip(
                text = option.text,
                selected = option.selected,
                onClick = { onSelect(index) },
            )
        }
    }
}

private fun <T : Enum<T>> ImmutableList<SituationChipOption<T>>.toChipContentOptions(selectedValue: T?): ImmutableList<SituationChipContentOption> =
    map { option ->
        SituationChipContentOption(
            text = option.text,
            selected = selectedValue == option.value,
        )
    }.toImmutableList()

@Composable
private fun SituationChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    PrezelTouchArea(
        onClick = onClick,
        shape = PrezelTheme.shapes.V8,
        isUseRipple = false,
    ) {
        PrezelChip(
            text = text,
            iconResId = if (selected) PrezelIcons.Check else null,
            type = ChipType.OUTLINED,
            size = ChipSize.REGULAR,
            state = if (selected) ChipState.ACTIVE else ChipState.DEFAULT,
        )
    }
}

@BasicPreview
@Composable
private fun PresentationSituationScreenPreview() {
    PrezelTheme {
        PresentationSituationScreen(
            uiState = AnalysisFlowUiState(
                step = AnalysisFlowStep.PRESENTATION_SITUATION,
                form = AnalysisForm(
                    category = Category.EDUCATION,
                    purpose = Purpose.CONTENT_DELIVERY,
                    style = Style.CALM,
                    audience = Audience.EXPERT,
                ),
            ),
            onSelectCategory = {},
            onSelectPurpose = {},
            onSelectStyle = {},
            onSelectAudience = {},
            onNext = {},
            onBack = {},
        )
    }
}

@BasicPreview
@Composable
private fun CategoryOptionGridPreview() {
    PrezelTheme {
        CategoryOptionGrid(
            selectedValue = Category.EDUCATION,
            options = categoryOptions(),
            onSelect = {},
        )
    }
}

@BasicPreview
@Composable
private fun PurposeOptionsPreview() {
    PrezelTheme {
        ChipOptionsContent(
            options = purposeOptions().toChipContentOptions(Purpose.CONTENT_DELIVERY),
            onSelect = {},
        )
    }
}

@BasicPreview
@Composable
private fun StyleOptionsPreview() {
    PrezelTheme {
        ChipOptionsContent(
            options = styleOptions().toChipContentOptions(Style.CALM),
            onSelect = {},
        )
    }
}

@BasicPreview
@Composable
private fun AudienceOptionsPreview() {
    PrezelTheme {
        ChipOptionsContent(
            options = audienceOptions().toChipContentOptions(Audience.EXPERT),
            onSelect = {},
        )
    }
}
