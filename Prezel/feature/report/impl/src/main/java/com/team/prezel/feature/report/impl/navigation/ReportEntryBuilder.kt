package com.team.prezel.feature.report.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.feature.report.api.ReportNavKey
import com.team.prezel.feature.report.impl.ReportScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureReportEntryBuilder() {
    entry<ReportNavKey> {
        ReportScreen()
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureReportModule {
    @IntoSet
    @Provides
    fun provideFeatureReportEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            featureReportEntryBuilder()
        }
}
