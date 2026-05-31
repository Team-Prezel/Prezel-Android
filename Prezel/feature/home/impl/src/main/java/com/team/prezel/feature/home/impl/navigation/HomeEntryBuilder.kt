package com.team.prezel.feature.home.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.analysis.api.AnalysisNavKey
import com.team.prezel.feature.analysis.api.AnalysisStartType
import com.team.prezel.feature.feedback.api.FeedbackNavKey
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.home.impl.main.HomeScreen
import com.team.prezel.feature.practice.api.PracticeNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureHomeEntryBuilder() {
    entry<HomeNavKey> {
        val navigator = LocalNavigator.current

        HomeScreen(
            navigateToPracticeRecording = { presentationId ->
                navigator.navigate(PracticeNavKey(presentationId = presentationId))
            },
            navigateToFileUploadAnalysis = {
                navigator.navigate(AnalysisNavKey.Schedule(startType = AnalysisStartType.FILE_UPLOAD))
            },
            navigateToVoiceRecordingAnalysis = {
                navigator.navigate(AnalysisNavKey.Schedule(startType = AnalysisStartType.VOICE_RECORDING))
            },
            navigateToAnalyzePresentation = { presentationId, isPast ->
                navigator.navigate(AnalysisNavKey.ReRecording(presentationId = presentationId, isPast = isPast))
            },
            navigateToFeedback = { presentationId, title, isPast ->
                navigator.navigate(
                    FeedbackNavKey(
                        presentationId = presentationId,
                        title = title,
                        isPast = isPast,
                    ),
                )
            },
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureHomeModule {
    @IntoSet
    @Provides
    fun provideFeatureHomeEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            featureHomeEntryBuilder()
        }
}
