package com.team.prezel.feature.home.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.home.impl.HomeScreen
import com.team.prezel.feature.home.impl.practice.PracticeRecordingScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureHomeEntryBuilder() {
    entry<HomeNavKey> {
        HomeScreen()
    }

    entry<PracticeRecordingNavKey> {
        val navigator = LocalNavigator.current

        PracticeRecordingScreen(onBack = navigator::goBack)
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
