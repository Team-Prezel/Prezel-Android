package com.team.prezel.feature.profile.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.profile.api.ProfileNavKey
import com.team.prezel.feature.profile.impl.ProfileScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureProfileEntryBuilder() {
    entry<ProfileNavKey.Create> {
        val navigator = LocalNavigator.current

        ProfileScreen(
            navigateToHome = { navigator.replaceRoot(HomeNavKey) },
            onBack = { navigator.goBack() },
        )
    }

    entry<ProfileNavKey.Edit> {
        val navigator = LocalNavigator.current

        ProfileScreen(
            navigateToHome = { navigator.replaceRoot(HomeNavKey) },
            onBack = { navigator.goBack() },
        )
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
