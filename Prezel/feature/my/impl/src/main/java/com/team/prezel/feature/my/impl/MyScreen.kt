package com.team.prezel.feature.my.impl

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
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.navigation.LocalNavigator
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.feature.login.api.LoginNavKey
import com.team.prezel.feature.my.impl.contract.MyUiEffect
import com.team.prezel.feature.my.impl.contract.MyUiState
import com.team.prezel.feature.my.impl.model.MyUiMessage

@Composable
internal fun MyScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current
    val resources = LocalResources.current
    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                MyUiEffect.NavigateToLogin -> navigator.replaceRoot(LoginNavKey)
                is MyUiEffect.ShowMessage ->
                    snackbarHostState.showPrezelSnackbar(resources.getString(effect.message.toTextRes()))
            }
        }
    }

    MyScreenContent(
        uiState = uiState,
        onLogout = {},
        onWithdraw = {},
        modifier = modifier,
    )
}

@Composable
private fun MyScreenContent(
    uiState: MyUiState,
    onLogout: () -> Unit,
    onWithdraw: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
                onClick = onLogout,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("로그아웃")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onWithdraw,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("회원탈퇴")
            }
        }
    }
}

private fun MyUiMessage.toTextRes(): Int =
    when (this) {
        MyUiMessage.AUTHENTICATION_EXPIRED -> R.string.feature_my_impl_authentication_expired
        MyUiMessage.LOGOUT_FAILED -> R.string.feature_my_impl_logout_failed
        MyUiMessage.WITHDRAW_FAILED -> R.string.feature_my_impl_withdraw_failed
    }
