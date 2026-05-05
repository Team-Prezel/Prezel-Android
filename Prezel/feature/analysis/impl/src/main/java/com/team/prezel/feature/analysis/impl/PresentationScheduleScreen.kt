package com.team.prezel.feature.analysis.impl

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.datepicker.PrezelDatePicker
import com.team.prezel.core.designsystem.component.textfield.PrezelTextField
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.analysis.impl.component.AnalysisStepLayout
import com.team.prezel.feature.analysis.impl.component.AnalysisStepTitle
import com.team.prezel.feature.analysis.impl.contract.AnalysisFlowUiState
import com.team.prezel.feature.analysis.impl.contract.AnalysisForm
import kotlinx.datetime.LocalDate

@Composable
internal fun PresentationScheduleScreen(
    uiState: AnalysisFlowUiState,
    onTitleChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    PresentationScheduleScreen(
        form = uiState.form,
        progress = uiState.progress,
        buttonEnabled = uiState.canMoveNext,
        onTitleChange = onTitleChange,
        onDateChange = onDateChange,
        onNext = onNext,
        onBack = onBack,
    )
}

@Composable
private fun PresentationScheduleScreen(
    form: AnalysisForm,
    progress: Float,
    buttonEnabled: Boolean,
    onTitleChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    val showDatePicker = rememberSaveable { mutableStateOf(false) }
    val dateFieldInteractionSource = remember { MutableInteractionSource() }
    val datePickerTitle = stringResource(R.string.feature_analysis_impl_presentation_date_label)

    if (showDatePicker.value) {
        PrezelDatePicker(
            title = datePickerTitle,
            initialSelectedDate = form.presentationDate.toLocalDateOrNull(),
            onClose = { showDatePicker.value = false },
            onConfirm = { selectedDate ->
                onDateChange(selectedDate.toPresentationDateText())
                showDatePicker.value = false
            },
        )
        return
    }

    AnalysisStepLayout(
        title = stringResource(R.string.feature_analysis_impl_schedule_title),
        progress = progress,
        buttonText = stringResource(R.string.feature_analysis_impl_next),
        buttonEnabled = buttonEnabled,
        onButtonClick = onNext,
        onBack = onBack,
    ) {
        AnalysisStepTitle(
            title = stringResource(R.string.feature_analysis_impl_schedule_headline),
            description = stringResource(R.string.feature_analysis_impl_schedule_description),
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        PrezelTextField(
            label = stringResource(R.string.feature_analysis_impl_presentation_name_label),
            value = form.presentationTitle,
            onValueChange = onTitleChange,
            placeholder = stringResource(R.string.feature_analysis_impl_presentation_name_placeholder),
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.feature_analysis_impl_presentation_date_label),
                style = PrezelTheme.typography.body3Medium,
                color = PrezelTheme.colors.textMedium,
                maxLines = 1,
            )

            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))

            Box(modifier = Modifier.fillMaxWidth()) {
                PrezelTextField(
                    value = form.presentationDate,
                    onValueChange = {},
                    placeholder = stringResource(R.string.feature_analysis_impl_presentation_date_placeholder),
                    trailingIcon = {
                        Icon(
                            painter = painterResource(PrezelIcons.Calendar),
                            contentDescription = datePickerTitle,
                        )
                    },
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { showDatePicker.value = true },
                        ),
                )
            }
        }
    }
}

private fun LocalDate.toPresentationDateText(): String = "%04d년 %02d월 %02d일".format(year, month.ordinal + 1, day)

private fun String.toLocalDateOrNull(): LocalDate? =
    runCatching {
        val (year, month, day) = split("년 ", "월 ", "일")

        LocalDate(
            year = year.toInt(),
            month = month.toInt(),
            day = day.toInt(),
        )
    }.getOrNull()

@BasicPreview
@Composable
private fun PresentationScheduleScreenPreview() {
    PrezelTheme {
        PresentationScheduleScreen(
            form = AnalysisForm(),
            progress = 0.25f,
            buttonEnabled = false,
            onNext = {},
            onBack = {},
            onTitleChange = {},
            onDateChange = {},
        )
    }
}

@BasicPreview
@Composable
private fun PresentationScheduleScreenDateSelectedPreview() {
    PrezelTheme {
        PresentationScheduleScreen(
            form = AnalysisForm(
                presentationTitle = "졸업 발표",
                presentationDate = "2026년 05월 09일",
            ),
            progress = 0.25f,
            buttonEnabled = true,
            onNext = {},
            onBack = {},
            onTitleChange = {},
            onDateChange = {},
        )
    }
}
