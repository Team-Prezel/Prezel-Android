package com.team.prezel.feature.terms.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.profile.api.ProfileNavKey
import com.team.prezel.feature.terms.api.TermsNavKey
import com.team.prezel.feature.terms.impl.TermsScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureTermsEntryBuilder() {
    entry<TermsNavKey> {
        val navigator = LocalNavigator.current

        TermsScreen(
            navigateBack = {
                navigator.goBack()
            },
            navigateToProfile = {
                navigator.navigate(ProfileNavKey.Create)
            },
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureTermsModule {
    @IntoSet
    @Provides
    fun provideFeatureTermsEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            featureTermsEntryBuilder()
        }
}
