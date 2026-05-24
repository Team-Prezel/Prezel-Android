package com.team.prezel.feature.analysis.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.analysis.api.AnalysisNavKey
import com.team.prezel.feature.analysis.impl.AnalysisScreen
import com.team.prezel.feature.home.api.HomeNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureAnalysisEntryBuilder() {
    entry<AnalysisNavKey.Create> {
        val navigator = LocalNavigator.current

        AnalysisScreen(
            onBack = { navigator.replaceRoot(HomeNavKey) },
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureAnalysisModule {
    @IntoSet
    @Provides
    fun provideFeatureAnalysisEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            featureAnalysisEntryBuilder()
        }
}
