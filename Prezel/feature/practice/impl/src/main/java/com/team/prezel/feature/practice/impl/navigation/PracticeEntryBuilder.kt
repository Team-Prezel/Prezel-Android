package com.team.prezel.feature.practice.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.practice.api.PracticeNavKey
import com.team.prezel.feature.practice.impl.PracticeRecordingScreen
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
            navigateToHome = { navigator.replaceRoot(HomeNavKey) },
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
