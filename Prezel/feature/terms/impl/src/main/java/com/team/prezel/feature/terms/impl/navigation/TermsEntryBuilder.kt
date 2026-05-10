package com.team.prezel.feature.terms.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.profile.api.ProfileNavKey
import com.team.prezel.feature.terms.api.TermsDocumentType
import com.team.prezel.feature.terms.api.TermsNavKey
import com.team.prezel.feature.terms.impl.BuildConfig
import com.team.prezel.feature.terms.impl.TermsScreen
import com.team.prezel.feature.terms.impl.component.TermsDetailModal
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureTermsEntryBuilder() {
    entry<TermsNavKey.List> {
        val navigator = LocalNavigator.current

        TermsScreen(
            navigateBack = {
                navigator.goBack()
            },
            navigateToTermsOfServiceDetail = {
                navigator.navigate(TermsNavKey.Detail(TermsDocumentType.TERMS_OF_SERVICE))
            },
            navigateToPrivacyPolicyDetail = {
                navigator.navigate(TermsNavKey.Detail(TermsDocumentType.PRIVACY_POLICY))
            },
            navigateToProfile = {
                navigator.navigate(ProfileNavKey.Create)
            },
        )
    }

    entry<TermsNavKey.Detail> { key ->
        val navigator = LocalNavigator.current

        TermsDetailModal(
            url = key.document.toUrl(),
            onDismiss = { navigator.goBack() },
        )
    }
}

private fun TermsDocumentType.toUrl(): String =
    when (this) {
        TermsDocumentType.TERMS_OF_SERVICE -> BuildConfig.TERMS_OF_SERVICE_URL
        TermsDocumentType.PRIVACY_POLICY -> BuildConfig.PRIVACY_POLICY_URL
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
