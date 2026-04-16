package com.team.prezel.feature.profile.impl

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.PrezelAvatar
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.actions.button.PrezelIconButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.component.modal.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.component.textfield.PrezelTextField
import com.team.prezel.core.designsystem.component.textfield.PrezelTextFieldFeedback
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.profile.User
import com.team.prezel.core.ui.LocalSnackbarHostState
import com.team.prezel.core.ui.advancedImePadding
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
            } else {
                viewModel.onIntent(ProfileUiIntent.OnProfileImageChanged(profileUrl = ""))
            }
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
            nicknameFeedback = fetchedState?.nicknameValidation?.toNicknameFeedback() ?: PrezelTextFieldFeedback.NO_MESSAGE,
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
    nicknameFeedback: PrezelTextFieldFeedback,
    onNicknameChanged: (String) -> Unit,
    onClickProfileImage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = PrezelTheme.spacing.V20)
            .padding(top = PrezelTheme.spacing.V16),
    ) {
        Avatar(
            profileUrl = profileUrl,
            isDefaultProfileImage = isDefaultProfileImage,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onClickProfileImage,
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        PrezelTextField(
            value = nickname,
            onValueChange = onNicknameChanged,
            label = stringResource(R.string.feature_profile_impl_nickname_text_field_label),
            placeholder = stringResource(R.string.feature_profile_impl_nickname_text_field_placeholder),
            modifier = Modifier.fillMaxWidth(),
            feedback = nicknameFeedback,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
            ),
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
    }
}

@Composable
private fun Avatar(
    profileUrl: String,
    isDefaultProfileImage: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(modifier = modifier) {
        PrezelAvatar(
            imageUrl = profileUrl,
            contentDescription = stringResource(R.string.feature_profile_impl_profile_image_content_description),
        )

        PrezelIconButton(
            iconResId = PrezelIcons.Plus,
            type = ButtonType.FILLED,
            size = ButtonSize.SMALL,
            hierarchy = ButtonHierarchy.PRIMARY,
            isRounded = true,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .rotate(if (isDefaultProfileImage) 0f else 45f),
            onClick = onClick,
        )
    }
}

@Composable
private fun NicknameValidationState.toNicknameFeedback(): PrezelTextFieldFeedback =
    when (this) {
        NicknameValidationState.Unchecked,
        NicknameValidationState.Checking,
        NicknameValidationState.TooLong,
        -> PrezelTextFieldFeedback.NO_MESSAGE

        NicknameValidationState.Available -> PrezelTextFieldFeedback.Good(
            message = stringResource(R.string.feature_profile_impl_nickname_helper_available),
        )

        NicknameValidationState.TooShort -> PrezelTextFieldFeedback.Bad(
            message = stringResource(R.string.feature_profile_impl_nickname_helper_too_short),
        )

        NicknameValidationState.Duplicated -> PrezelTextFieldFeedback.Bad(
            message = stringResource(R.string.feature_profile_impl_nickname_helper_duplicated),
        )

        NicknameValidationState.InvalidCharacter -> PrezelTextFieldFeedback.Bad(
            message = stringResource(R.string.feature_profile_impl_nickname_helper_unavailable),
        )
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
