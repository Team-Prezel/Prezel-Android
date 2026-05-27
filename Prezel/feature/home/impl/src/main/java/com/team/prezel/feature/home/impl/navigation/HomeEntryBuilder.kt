package com.team.prezel.feature.home.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.analysis.api.AnalysisNavKey
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.home.impl.main.HomeScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureHomeEntryBuilder() {
    entry<HomeNavKey> {
        val navigator = LocalNavigator.current

        HomeScreen(
            navigateToFileUploadAnalysis = {
                navigator.navigate(AnalysisNavKey.Schedule())
            },
            navigateToVoiceRecordingAnalysis = {
                navigator.navigate(AnalysisNavKey.Schedule())
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
