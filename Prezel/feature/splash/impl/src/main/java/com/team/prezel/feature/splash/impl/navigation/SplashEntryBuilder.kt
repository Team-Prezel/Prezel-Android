package com.team.prezel.feature.splash.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.core.navigation.LocalSharedTransitionScope
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.login.api.LoginNavKey
import com.team.prezel.feature.profile.api.ProfileNavKey
import com.team.prezel.feature.splash.api.SplashNavKey
import com.team.prezel.feature.splash.impl.SplashScreen
import com.team.prezel.feature.terms.api.TermsNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureSplashEntryBuilder() {
    entry<SplashNavKey> {
        val navigator = LocalNavigator.current

        with(LocalSharedTransitionScope.current) {
            SplashScreen(
                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                navigateToHome = {
                    navigator.replaceRoot(HomeNavKey)
                },
                navigateToLogin = {
                    navigator.replaceRoot(LoginNavKey)
                },
                navigateToTerms = {
                    navigator.replaceRoot(LoginNavKey)
                    navigator.navigate(TermsNavKey)
                },
                navigateToCreateProfile = {
                    navigator.replaceRoot(LoginNavKey)
                    navigator.navigate(ProfileNavKey.Create)
                },
            )
        }
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureSplashModule {
    @IntoSet
    @Provides
    fun provideFeatureSplashEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            featureSplashEntryBuilder()
        }
}
