package com.team.prezel.feature.report.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.core.navigation.Navigator
import com.team.prezel.feature.analysis.api.AnalysisNavKey
import com.team.prezel.feature.feedback.api.FeedbackNavKey
import com.team.prezel.feature.report.api.ReportNavKey
import com.team.prezel.feature.report.impl.accuracydetail.AccuracyDetailScreen
import com.team.prezel.feature.report.impl.accuracydetail.AccuracyDetailTab
import com.team.prezel.feature.report.impl.accuracydetail.AccuracyDetailViewModel
import com.team.prezel.feature.report.impl.report.AnalysisReportScreen
import com.team.prezel.feature.report.impl.report.AnalysisReportViewModel
import com.team.prezel.feature.report.impl.script.ScriptScreen
import com.team.prezel.feature.report.impl.script.ScriptViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureAnalysisReportEntryBuilder() {
    reportEntry()
    scriptCorrectionEntry()
    accuracyDetailEntry()
}

private fun EntryProviderScope<NavKey>.reportEntry() {
    entry<ReportNavKey> { key ->
        val navigator = LocalNavigator.current

        AnalysisReportScreen(
            onBack = { navigator.goBack() },
            navigateToAnalysisScript = { presentationId, isPast ->
                navigator.navigateToAnalysisScript(
                    presentationId = presentationId,
                    isPast = isPast,
                )
            },
            navigateToAnalysisRecording = { presentationId, isPast ->
                navigator.navigateToAnalysisRecording(
                    presentationId = presentationId,
                    isPast = isPast,
                )
            },
            navigateToSpeechAccuracy = { analysisResultId ->
                navigator.navigateToAccuracyDetail(
                    analysisResultId = analysisResultId,
                    initialTab = AccuracyDetailTab.SPEECH,
                )
            },
            navigateToScriptMatch = { analysisResultId ->
                navigator.navigateToAccuracyDetail(
                    analysisResultId = analysisResultId,
                    initialTab = AccuracyDetailTab.SCRIPT_MATCH,
                )
            },
            navigateToSelfFeedbackWrite = { presentationId, title, isPast ->
                navigator.navigateToSelfFeedbackWrite(
                    presentationId = presentationId,
                    title = title,
                    isPast = isPast,
                )
            },
            navigateToScriptAnalysis = { analysisResultId ->
                navigator.navigateToScriptAnalysis(analysisResultId = analysisResultId)
            },
            viewModel = hiltViewModel<AnalysisReportViewModel, AnalysisReportViewModel.Factory>(
                creationCallback = { factory -> factory.create(key) },
            ),
        )
    }
}

private fun EntryProviderScope<NavKey>.scriptCorrectionEntry() {
    entry<ReportInnerNavKey.ScriptCorrection> { key ->
        val navigator = LocalNavigator.current

        ScriptScreen(
            onClose = { navigator.goBack() },
            viewModel = hiltViewModel<ScriptViewModel, ScriptViewModel.Factory>(
                creationCallback = { factory -> factory.create(analysisResultId = key.analysisResultId) },
            ),
        )
    }
}

private fun Navigator.navigateToAnalysisScript(
    presentationId: Long,
    isPast: Boolean,
) {
    navigate(
        AnalysisNavKey.ReWritingScript(
            presentationId = presentationId,
            isPast = isPast,
        ),
    )
}

private fun Navigator.navigateToAnalysisRecording(
    presentationId: Long,
    isPast: Boolean,
) {
    navigate(
        AnalysisNavKey.ReRecording(
            presentationId = presentationId,
            isPast = isPast,
        ),
    )
}

private fun Navigator.navigateToAccuracyDetail(
    analysisResultId: Long,
    initialTab: AccuracyDetailTab,
) {
    navigate(
        ReportInnerNavKey.AccuracyDetail(
            analysisResultId = analysisResultId,
            initialTab = initialTab,
        ),
    )
}

private fun Navigator.navigateToSelfFeedbackWrite(
    presentationId: Long,
    title: String,
    isPast: Boolean,
) {
    navigate(
        FeedbackNavKey(
            presentationId = presentationId,
            title = title,
            isPast = isPast,
        ),
    )
}

private fun Navigator.navigateToScriptAnalysis(analysisResultId: Long) {
    navigate(ReportInnerNavKey.ScriptCorrection(analysisResultId = analysisResultId))
}

private fun EntryProviderScope<NavKey>.accuracyDetailEntry() {
    entry<ReportInnerNavKey.AccuracyDetail> { key ->
        val navigator = LocalNavigator.current

        AccuracyDetailScreen(
            onClose = { navigator.goBack() },
            initialTab = key.initialTab,
            viewModel = hiltViewModel<AccuracyDetailViewModel, AccuracyDetailViewModel.Factory>(
                creationCallback = { factory -> factory.create(analysisResultId = key.analysisResultId) },
            ),
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureReportModule {
    @IntoSet
    @Provides
    fun provideFeatureReportEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            featureAnalysisReportEntryBuilder()
        }
}
