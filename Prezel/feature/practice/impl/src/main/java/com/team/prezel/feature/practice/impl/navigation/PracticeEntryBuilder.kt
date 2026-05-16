package com.team.prezel.feature.practice.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.practice.api.PracticeNavKey
import com.team.prezel.feature.practice.impl.analysis.PracticeAnalysisScreen
import com.team.prezel.feature.practice.impl.analysis.PracticeAnalysisViewModel
import com.team.prezel.feature.practice.impl.recording.PracticeRecordingScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featurePracticeEntryBuilder() {
    entry<PracticeNavKey> {
        val navigator = LocalNavigator.current

        PracticeRecordingScreen(
            onBack = navigator::goBack,
            navigateToAnalysis = { recordingFilePath, referenceText ->
                navigator.navigate(
                    PracticeAnalysisNavKey(
                        recordingFilePath = recordingFilePath,
                        referenceText = referenceText,
                    ),
                )
            },
        )
    }

    entry<PracticeAnalysisNavKey> { key ->
        val navigator = LocalNavigator.current

        PracticeAnalysisScreen(
            onRetry = navigator::goBack,
            onComplete = { navigator.replaceRoot(HomeNavKey) },
            viewModel = hiltViewModel<PracticeAnalysisViewModel, PracticeAnalysisViewModel.Factory> { factory ->
                factory.create(
                    recordingFilePath = key.recordingFilePath,
                    referenceText = key.referenceText,
                )
            },
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeaturePracticeModule {
    @IntoSet
    @Provides
    fun provideFeaturePracticeEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            featurePracticeEntryBuilder()
        }
}
