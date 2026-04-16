package com.team.prezel.feature.profile.impl

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.modal.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.profile.User
import com.team.prezel.core.ui.LocalSnackbarHostState
import com.team.prezel.core.ui.advancedImePadding
import com.team.prezel.feature.profile.impl.component.NicknameTextField
import com.team.prezel.feature.profile.impl.component.ProfileImageEditor
import com.team.prezel.feature.profile.impl.component.ProfileScreenTopAppBar
import com.team.prezel.feature.profile.impl.contract.NicknameValidationState
import com.team.prezel.feature.profile.impl.contract.ProfileUiEffect
import com.team.prezel.feature.profile.impl.contract.ProfileUiIntent
import com.team.prezel.feature.profile.impl.contract.ProfileUiState
import com.team.prezel.feature.profile.impl.model.ProfileUiMessage

@Composable
internal fun ProfileScreen(
    navigateToHome: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current
    val resources = LocalResources.current
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        viewModel.onIntent(ProfileUiIntent.OnProfileImageChanged(profileUrl = uri.toString()))
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(ProfileUiIntent.FetchData)

        viewModel.uiEffect.collect { effect ->
            when (effect) {
                ProfileUiEffect.NavigateToHome -> navigateToHome()
                ProfileUiEffect.OnBack -> onBack()
                is ProfileUiEffect.ShowMessage -> {
                    val resId = when (effect.message) {
                        ProfileUiMessage.CHECK_NICKNAME_FAILED -> R.string.feature_profile_impl_check_nickname_failed_message
                        ProfileUiMessage.FETCH_USER_INFO_FAILED -> R.string.feature_profile_impl_fetch_user_info_failed_message
                    }
                    snackbarHostState.showPrezelSnackbar(message = resources.getString(resId))
                }
            }
        }
    }

    ProfileScreen(
        uiState = uiState,
        onNicknameChanged = { nickname ->
            viewModel.onIntent(ProfileUiIntent.OnNicknameChanged(nickname.filterNot(Char::isWhitespace)))
        },
        onClickProfileImage = {
            if (uiState.canPhotoPickerLaunch()) {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
                return@ProfileScreen
            }

            viewModel.onIntent(ProfileUiIntent.OnProfileImageChanged(profileUrl = ""))
        },
        onClickSubmit = { viewModel.onIntent(ProfileUiIntent.OnClickSubmit) },
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
private fun ProfileScreen(
    uiState: ProfileUiState,
    onNicknameChanged: (String) -> Unit,
    onClickProfileImage: () -> Unit,
    onClickSubmit: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val fetchedState = uiState as? ProfileUiState.Fetched
    val submitButtonText = stringResource(R.string.feature_profile_impl_submit_button_text)

    Column(modifier = modifier.fillMaxSize()) {
        ProfileScreenTopAppBar(
            isCreate = uiState is ProfileUiState.Create,
            onBack = onBack,
        )

        ProfileScreenContent(
            profileUrl = fetchedState?.profileImage?.url.orEmpty(),
            isDefaultProfileImage = fetchedState?.profileImage?.isDefault ?: true,
            nickname = fetchedState?.nickname.orEmpty(),
            onNicknameChanged = onNicknameChanged,
            nicknameValidationState = fetchedState?.nicknameValidation ?: NicknameValidationState.Unchecked,
            onClickProfileImage = onClickProfileImage,
            modifier = Modifier.weight(1f),
        )

        PrezelButtonArea(
            showBackground = true,
            modifier = Modifier.advancedImePadding(),
        ) {
            MainButton(
                label = submitButtonText,
                enabled = fetchedState?.submitButtonEnabled ?: false,
                onClick = onClickSubmit,
            )
        }
    }
}

@Composable
private fun ProfileScreenContent(
    profileUrl: String,
    isDefaultProfileImage: Boolean,
    nickname: String,
    nicknameValidationState: NicknameValidationState,
    onNicknameChanged: (String) -> Unit,
    onClickProfileImage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = PrezelTheme.spacing.V20)
            .padding(top = PrezelTheme.spacing.V16),
    ) {
        ProfileImageEditor(
            profileUrl = profileUrl,
            isDefaultProfileImage = isDefaultProfileImage,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onClickProfileImage,
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        NicknameTextField(
            nickname = nickname,
            onNicknameChanged = onNicknameChanged,
            nicknameValidationState = nicknameValidationState,
        )
    }
}

@BasicPreview
@Composable
private fun CreateProfileScreenPreview() {
    PrezelTheme {
        ProfileScreen(
            uiState = ProfileUiState.Create(),
            onNicknameChanged = {},
            onClickProfileImage = {},
            onClickSubmit = {},
            onBack = {},
        )
    }
}

@BasicPreview
@Composable
private fun EditProfileScreenPreview() {
    PrezelTheme {
        ProfileScreen(
            uiState = ProfileUiState.Edit(
                originalNickname = "",
                nickname = "",
                originalProfileImage = User.ProfileImage(url = "", isDefault = true),
                profileImage = User.ProfileImage(url = "", isDefault = true),
            ),
            onNicknameChanged = {},
            onClickProfileImage = {},
            onClickSubmit = {},
            onBack = {},
        )
    }
}
