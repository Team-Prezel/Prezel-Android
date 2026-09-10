package com.team.prezel.feature.history.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.analysis.api.AnalysisNavKey
import com.team.prezel.feature.history.api.HistoryNavKey
import com.team.prezel.feature.history.impl.HistoryFailedAnalysisScreen
import com.team.prezel.feature.history.impl.HistoryScreen
import com.team.prezel.feature.report.api.ReportEntrySource
import com.team.prezel.feature.report.api.ReportNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import kotlinx.serialization.Serializable

internal fun EntryProviderScope<NavKey>.featureHistoryEntryBuilder() {
    entry<HistoryNavKey> {
        val navigator = LocalNavigator.current

        HistoryScreen(
            navigateToReport = { presentationId, isPast ->
                navigator.navigate(
                    ReportNavKey(
                        presentationId = presentationId,
                        isPast = isPast,
                        entrySource = ReportEntrySource.HISTORY,
                    ),
                )
            },
            navigateToFailedAnalysis = { presentationId, analysisResultId, errorType, script, audioUrl ->
                navigator.navigate(
                    HistoryFailedAnalysisNavKey(
                        presentationId = presentationId,
                        analysisResultId = analysisResultId,
                        errorType = errorType,
                        script = script,
                        audioUrl = audioUrl,
                    ),
                )
            },
            navigateToAnalysis = {
                navigator.navigate(AnalysisNavKey.Schedule())
            },
        )
    }

    entry<HistoryFailedAnalysisNavKey> { key ->
        val navigator = LocalNavigator.current

        HistoryFailedAnalysisScreen(
            presentationId = key.presentationId,
            analysisResultId = key.analysisResultId,
            errorTypeValue = key.errorType,
            script = key.script,
            audioUrl = key.audioUrl,
            onClose = { navigator.goBack() },
            navigateToHistory = { navigator.replaceRoot(HistoryNavKey) },
        )
    }
}

@Serializable
private data class HistoryFailedAnalysisNavKey(
    val presentationId: Long,
    val analysisResultId: Long?,
    val errorType: String?,
    val script: String?,
    val audioUrl: String?,
) : NavKey

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureHistoryModule {
    @IntoSet
    @Provides
    fun provideFeatureHistoryEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            featureHistoryEntryBuilder()
        }
}
