package com.team.prezel.feature.profile.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.modal.snackbar.showPrezelSnackbar
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.core.ui.LocalSnackbarHostState
import com.team.prezel.feature.login.api.LoginNavKey
import com.team.prezel.feature.profile.impl.contract.ProfileUiEffect
import com.team.prezel.feature.profile.impl.model.ProfileUiMessage

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current
    val resources = LocalResources.current
    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                ProfileUiEffect.NavigateToLogin -> navigator.replaceRoot(LoginNavKey)
                is ProfileUiEffect.ShowMessage -> {
                    val resId = when (effect.message) {
                        ProfileUiMessage.AUTHENTICATION_EXPIRED -> R.string.feature_profile_impl_authentication_expired
                        ProfileUiMessage.LOGOUT_FAILED -> R.string.feature_profile_impl_logout_failed
                        ProfileUiMessage.WITHDRAW_FAILED -> R.string.feature_profile_impl_withdraw_failed
                    }

                    snackbarHostState.showPrezelSnackbar(resources.getString(resId))
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = viewModel::logout,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("로그아웃")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = viewModel::withdraw,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("회원탈퇴")
            }
        }
    }
}
