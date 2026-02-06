package com.team.prezel.feature.history.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.feature.history.api.HistoryNavKey
import com.team.prezel.feature.history.impl.HistoryScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureHistoryEntryBuilder() {
    entry<HistoryNavKey> {
        HistoryScreen()
    }
}

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
