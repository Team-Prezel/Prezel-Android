package com.team.prezel.feature.profile.impl

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.feedback.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.profile.User
import com.team.prezel.core.ui.state.LocalSnackbarHostState
import com.team.prezel.core.ui.util.advancedImePadding
import com.team.prezel.feature.profile.impl.component.NicknameTextField
import com.team.prezel.feature.profile.impl.component.ProfileImageEditor
import com.team.prezel.feature.profile.impl.component.ProfileScreenTopAppBar
import com.team.prezel.feature.profile.impl.contract.ProfileUiEffect
import com.team.prezel.feature.profile.impl.contract.ProfileUiIntent
import com.team.prezel.feature.profile.impl.contract.ProfileUiState
import com.team.prezel.feature.profile.impl.model.NicknameValidationState
import com.team.prezel.feature.profile.impl.model.ProfileUiMessage

@Composable
internal fun ProfileScreen(
    isNewProfile: Boolean,
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
        viewModel.onIntent(ProfileUiIntent.UpdateProfileImage(profileUrl = uri.toString()))
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(ProfileUiIntent.FetchData)

        viewModel.uiEffect.collect { effect ->
            when (effect) {
                ProfileUiEffect.NavigateToHome -> navigateToHome()
                ProfileUiEffect.NavigateToBack -> onBack()
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
        isNewProfile = isNewProfile,
        onNicknameChanged = { nickname -> viewModel.onIntent(ProfileUiIntent.UpdateNickname(nickname)) },
        onClickProfileImage = {
            if (uiState.shouldLaunchPhotoPicker) {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
                return@ProfileScreen
            }

            viewModel.onIntent(ProfileUiIntent.UpdateProfileImage(profileUrl = ""))
        },
        onClickSubmit = { viewModel.onIntent(ProfileUiIntent.SubmitProfile) },
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
private fun ProfileScreen(
    uiState: ProfileUiState,
    isNewProfile: Boolean,
    onNicknameChanged: (String) -> Unit,
    onClickProfileImage: () -> Unit,
    onClickSubmit: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentState = uiState as? ProfileUiState.Content
    val submitButtonText = stringResource(R.string.feature_profile_impl_submit_button_text)

    Column(modifier = modifier.fillMaxSize()) {
        ProfileScreenTopAppBar(
            isCreate = isNewProfile,
            onBack = onBack,
        )

        ProfileScreenContent(
            profileUrl = contentState?.profileImage?.url.orEmpty(),
            isDefaultProfileImage = contentState?.profileImage?.isDefault ?: true,
            nickname = contentState?.nickname.orEmpty(),
            onNicknameChanged = onNicknameChanged,
            nicknameValidationState = contentState?.nicknameValidation ?: NicknameValidationState.Unchecked,
            onClickProfileImage = onClickProfileImage,
            modifier = Modifier.weight(1f),
        )

        PrezelButtonArea(
            showBackground = true,
            modifier = Modifier.advancedImePadding(),
        ) {
            MainButton(
                label = submitButtonText,
                enabled = contentState?.submitButtonEnabled ?: false,
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
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
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
            uiState = ProfileUiState.Content(
                originalNickname = "",
                originalProfileImage = User.ProfileImage(url = "", isDefault = true),
                nickname = "",
                nicknameValidation = NicknameValidationState.Unchecked,
                profileImage = User.ProfileImage(url = "", isDefault = true),
            ),
            isNewProfile = true,
            onNicknameChanged = {},
            onClickProfileImage = {},
            onClickSubmit = {},
            onBack = {},
        )
    }
}
