package com.team.prezel.feature.setting.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.setting.api.DeleteAccountNavKey
import com.team.prezel.feature.setting.api.SettingNavKey
import com.team.prezel.feature.setting.impl.delete.DeleteAccountScreen
import com.team.prezel.feature.setting.impl.setting.SettingScreen
import com.team.prezel.feature.splash.api.SplashNavKey
import com.team.prezel.feature.terms.api.TermsDocumentType
import com.team.prezel.feature.terms.api.TermsNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureSettingEntryBuilder() {
    entry<SettingNavKey> {
        val navigator = LocalNavigator.current

        SettingScreen(
            navigateBack = { navigator.goBack() },
            navigateToDeleteAccount = { navigator.navigate(DeleteAccountNavKey) },
            navigateToSplash = { navigator.replaceRoot(SplashNavKey) },
            navigateToTermsOfService = {
                navigator.navigate(TermsNavKey.Detail(TermsDocumentType.TERMS_OF_SERVICE))
            },
            navigateToPrivacyPolicy = {
                navigator.navigate(TermsNavKey.Detail(TermsDocumentType.PRIVACY_POLICY))
            },
        )
    }

    entry<DeleteAccountNavKey> {
        val navigator = LocalNavigator.current

        DeleteAccountScreen(
            navigateBack = { navigator.goBack() },
            navigateToSplash = { navigator.replaceRoot(SplashNavKey) },
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureSettingModule {
    @IntoSet
    @Provides
    fun provideFeatureSettingEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            featureSettingEntryBuilder()
        }
}
