package com.team.prezel.feature.report.impl.component.body

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.PrezelAccordion
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.report.impl.R
import com.team.prezel.feature.report.impl.component.common.EmptyStateCard
import com.team.prezel.feature.report.impl.component.common.ReportSection
import com.team.prezel.feature.report.impl.model.QuestionUiModel
import com.team.prezel.feature.report.impl.preview.ReportPreviewUpcomingUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ExpectedQuestionsSection(questions: ImmutableList<QuestionUiModel>) {
    ReportSection(
        title = { ExpectedQuestionsSectionTitle() },
    ) {
        if (questions.isEmpty()) {
            EmptyStateCard(text = stringResource(R.string.feature_report_impl_empty_questions))
        } else {
            QuestionAccordionList(questions = questions)
        }
    }
}

@Composable
private fun ExpectedQuestionsSectionTitle() {
    Text(
        text = stringResource(R.string.feature_report_impl_section_expected_questions),
        style = PrezelTheme.typography.body2Bold,
        color = PrezelTheme.colors.textLarge,
    )
}

@Composable
private fun QuestionAccordionList(questions: ImmutableList<QuestionUiModel>) {
    Column(verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8)) {
        CompositionLocalProvider(
            LocalContentColor provides PrezelTheme.colors.textLarge,
        ) {
            questions.forEach { question ->
                QuestionAccordion(question = question)
            }
        }
    }
}

@Composable
private fun QuestionAccordion(question: QuestionUiModel) {
    PrezelAccordion(
        title = question.question,
        nested = false,
        initiallyExpanded = false,
    ) {
        QuestionAnswer(answer = question.answer)
    }
}

@Composable
private fun QuestionAnswer(answer: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrezelTheme.colors.bgMedium)
            .padding(PrezelTheme.spacing.V12),
    ) {
        Text(
            text = answer,
            style = PrezelTheme.typography.body3Regular,
            color = PrezelTheme.colors.textMedium,
        )
    }
}

@BasicPreview
@Composable
private fun ExpectedQuestionsSectionPreview() {
    PrezelTheme {
        ExpectedQuestionsSection(questions = ReportPreviewUpcomingUiState.expectedQuestions)
    }
}

@BasicPreview
@Composable
private fun EmptyExpectedQuestionsSectionPreview() {
    PrezelTheme {
        ExpectedQuestionsSection(questions = persistentListOf())
    }
}
