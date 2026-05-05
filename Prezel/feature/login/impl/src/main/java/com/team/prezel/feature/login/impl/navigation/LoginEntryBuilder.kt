package com.team.prezel.feature.login.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.auth.AuthManager
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.core.navigation.LocalSharedTransitionScope
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.login.api.LoginNavKey
import com.team.prezel.feature.login.impl.LoginScreen
import com.team.prezel.feature.profile.api.ProfileNavKey
import com.team.prezel.feature.terms.api.TermsNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureLoginEntryBuilder(authManager: AuthManager) {
    entry<LoginNavKey> {
        val navigator = LocalNavigator.current

        with(LocalSharedTransitionScope.current) {
            LoginScreen(
                authManager = authManager,
                navigateToHome = {
                    navigator.replaceRoot(HomeNavKey)
                },
                navigateToTerms = {
                    navigator.navigate(TermsNavKey)
                },
                navigateToCreateProfile = {
                    navigator.navigate(ProfileNavKey.Create)
                },
            )
        }
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureLoginModule {
    @IntoSet
    @Provides
    fun provideFeatureLoginEntryBuilder(authManager: AuthManager): EntryProviderScope<NavKey>.() -> Unit =
        {
            featureLoginEntryBuilder(authManager = authManager)
        }
}
