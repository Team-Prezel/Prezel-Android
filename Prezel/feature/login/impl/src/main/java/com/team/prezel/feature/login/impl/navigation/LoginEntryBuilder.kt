package com.team.prezel.feature.login.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.team.prezel.core.auth.AuthManager
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.core.navigation.LocalSharedTransitionScope
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.login.api.LoginNavKey
import com.team.prezel.feature.login.impl.LoginScreen
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
                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                authManager = authManager,
                navigateToHome = {
                    navigator.replaceRoot(HomeNavKey)
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
