package com.team.prezel.feature.profile.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.feature.profile.api.ProfileNavKey
import com.team.prezel.feature.profile.impl.ProfileScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

fun EntryProviderScope<NavKey>.featureProfileEntryBuilder() {
    entry<ProfileNavKey> {
        ProfileScreen()
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureProfileModule {
    @IntoSet
    @Provides
    fun provideFeatureProfileEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            featureProfileEntryBuilder()
        }
}
