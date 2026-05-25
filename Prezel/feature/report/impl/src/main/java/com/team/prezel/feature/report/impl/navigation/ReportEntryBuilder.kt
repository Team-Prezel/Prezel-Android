package com.team.prezel.feature.report.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.report.api.ReportNavKey
import com.team.prezel.feature.report.impl.analysis.AnalysisReportScreen
import com.team.prezel.feature.report.impl.analysis.AnalysisReportViewModel
import com.team.prezel.feature.report.impl.history.HistoryReportScreen
import com.team.prezel.feature.report.impl.history.HistoryReportViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureReportEntryBuilder() {
    entry<ReportNavKey.Analysis> { key ->
        val navigator = LocalNavigator.current

        AnalysisReportScreen(
            navigateToHome = { navigator.replaceRoot(HomeNavKey) },
            onBack = { navigator.goBack() },
            viewModel = hiltViewModel<AnalysisReportViewModel, AnalysisReportViewModel.Factory>(
                creationCallback = { factory -> factory.create(key.presentationId) },
            ),
        )
    }
    entry<ReportNavKey.History> { key ->
        val navigator = LocalNavigator.current

        HistoryReportScreen(
            viewModel = hiltViewModel<HistoryReportViewModel, HistoryReportViewModel.Factory>(
                creationCallback = { factory -> factory.create(key) },
            ),
            onBackClick = { navigator.goBack() },
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
            featureReportEntryBuilder()
        }
}
