package com.team.prezel.feature.report.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.analysis.api.AnalysisNavKey
import com.team.prezel.feature.feedback.api.FeedbackNavKey
import com.team.prezel.feature.report.api.ReportNavKey
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
    entry<ReportNavKey> { key ->
        val navigator = LocalNavigator.current

        AnalysisReportScreen(
            onBack = { navigator.goBack() },
            navigateToAnalysisScript = { presentationId, isPast ->
                navigator.navigate(
                    AnalysisNavKey.ReWritingScript(
                        presentationId = presentationId,
                        isPast = isPast,
                    ),
                )
            },
            navigateToAnalysisRecording = { presentationId, isPast ->
                navigator.navigate(
                    AnalysisNavKey.ReRecording(
                        presentationId = presentationId,
                        isPast = isPast,
                    ),
                )
            },
            navigateToSelfFeedbackWrite = { presentationId, title, isPast ->
                navigator.navigate(
                    FeedbackNavKey(
                        presentationId = presentationId,
                        title = title,
                        isPast = isPast,
                    ),
                )
            },
            navigateToScriptAnalysis = { analysisResultId ->
                navigator.navigate(ReportInnerNavKey.ScriptCorrection(analysisResultId = analysisResultId))
            },
            viewModel = hiltViewModel<AnalysisReportViewModel, AnalysisReportViewModel.Factory>(
                creationCallback = { factory -> factory.create(key) },
            ),
        )
    }
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
