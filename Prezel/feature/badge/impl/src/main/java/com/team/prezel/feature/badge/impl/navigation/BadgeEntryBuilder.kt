package com.team.prezel.feature.badge.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.badge.api.BadgeNavKey
import com.team.prezel.feature.badge.impl.BadgeScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureBadgeEntryBuilder() {
    entry<BadgeNavKey> {
        val navigator = LocalNavigator.current

        BadgeScreen(
            onBack = { navigator.goBack() },
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureBadgeModule {
    @IntoSet
    @Provides
    fun provideFeatureBadgeEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            featureBadgeEntryBuilder()
        }
}
