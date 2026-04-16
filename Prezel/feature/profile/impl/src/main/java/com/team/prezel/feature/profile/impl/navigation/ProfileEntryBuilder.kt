package com.team.prezel.feature.profile.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.team.prezel.core.model.profile.User
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.feature.home.api.HomeNavKey
import com.team.prezel.feature.profile.api.ProfileNavKey
import com.team.prezel.feature.profile.impl.ProfileScreen
import com.team.prezel.feature.profile.impl.ProfileViewModel
import com.team.prezel.feature.profile.impl.contract.ProfileUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

internal fun EntryProviderScope<NavKey>.featureProfileEntryBuilder() {
    entry<ProfileNavKey.Create> {
        val navigator = LocalNavigator.current

        ProfileScreen(
            navigateToHome = { navigator.replaceRoot(HomeNavKey) },
            onBack = { navigator.goBack() },
            viewModel = hiltViewModel<ProfileViewModel, ProfileViewModel.Factory>(
                creationCallback = { factory -> factory.create(initialState = ProfileUiState.Create()) },
            ),
        )
    }

    entry<ProfileNavKey.Edit> { key ->
        val navigator = LocalNavigator.current

        ProfileScreen(
            navigateToHome = { navigator.replaceRoot(HomeNavKey) },
            onBack = { navigator.goBack() },
            viewModel = hiltViewModel<ProfileViewModel, ProfileViewModel.Factory>(
                creationCallback = { factory ->
                    factory.create(
                        initialState = ProfileUiState.Edit(
                            originalNickname = key.nickname,
                            profileImage = User.ProfileImage(
                                url = key.profileUrl,
                                isDefault = key.isDefault,
                            ),
                        ),
                    )
                },
            ),
        )
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeatureProfileModule {
    @IntoSet
    @Provides
    fun provideFeatureProfileEntryBuilder(): EntryProviderScope<NavKey>.() -> Unit =
        {
            featureProfileEntryBuilder()
        }
}
