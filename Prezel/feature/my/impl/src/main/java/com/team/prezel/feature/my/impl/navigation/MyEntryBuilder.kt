package com.team.prezel.feature.my.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.my.api.MyNavKey
import com.team.prezel.feature.my.impl.MyScreen
import com.team.prezel.feature.profile.api.ProfileNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureMyEntryBuilder() {
    entry<MyNavKey> {
        val navigator = LocalNavigator.current

        MyScreen(
            navigateToEditProfile = { navigator.navigate(ProfileNavKey.Edit) },
            navigateToSetting = { /* navigator.navigate(ProfileNavKey.Setting) */ },
            navigateToBadge = { /* navigator.navigate(ProfileNavKey.Badge) */ },
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureMyModule {
    @IntoSet
    @Provides
    fun provideFeatureMyEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            featureMyEntryBuilder()
        }
}
