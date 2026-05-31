package com.team.prezel.feature.feedback.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.feedback.api.FeedbackNavKey
import com.team.prezel.feature.feedback.impl.FeedbackScreen
import com.team.prezel.feature.feedback.impl.FeedbackViewModel
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.report.api.ReportNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import java.util.UUID

internal fun EntryProviderScope<NavKey>.featureFeedbackEntryBuilder() {
    entry<FeedbackNavKey> { key ->
        val navigator = LocalNavigator.current

        FeedbackScreen(
            title = key.title,
            navigateBack = { navigator.goBack() },
            onSaveComplete = {
                if (key.returnToReportOnSave) {
                    navigator.navigate(
                        key = ReportNavKey(
                            presentationId = key.presentationId,
                            isPast = key.isPast,
                            refreshKey = UUID.randomUUID().toString(),
                        ),
                        clearStack = true,
                    )
                } else {
                    navigator.replaceRoot(HomeNavKey)
                }
            },
            viewModel = hiltViewModel<FeedbackViewModel, FeedbackViewModel.Factory>(
                creationCallback = { factory -> factory.create(key) },
            ),
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureFeedbackModule {
    @IntoSet
    @Provides
    fun provideFeatureFeedbackEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            featureFeedbackEntryBuilder()
        }
}
