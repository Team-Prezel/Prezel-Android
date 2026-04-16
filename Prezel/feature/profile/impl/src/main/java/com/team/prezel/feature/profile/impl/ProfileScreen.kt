package com.team.prezel.feature.profile.impl
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.prezel.core.designsystem.component.PrezelAvatar
import com.team.prezel.core.designsystem.component.actions.area.PrezelButtonArea
import com.team.prezel.core.designsystem.component.modal.snackbar.showPrezelSnackbar
import com.team.prezel.core.designsystem.component.textfield.PrezelTextField
import com.team.prezel.core.designsystem.component.textfield.PrezelTextFieldFeedback
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.LocalSnackbarHostState
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
    viewModel: ProfileViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current
    val resources = LocalResources.current

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                ProfileUiEffect.NavigateToHome -> navigateToHome()
                is ProfileUiEffect.ShowMessage -> {
                    val resId = when (effect.message) {
                        ProfileUiMessage.CHECK_NICKNAME_FAILED -> R.string.feature_profile_impl_check_nickname_failed
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
        onClickPrimaryAction = { viewModel.onIntent(ProfileUiIntent.OnClickSubmit) },
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
private fun ProfileScreen(
    uiState: ProfileUiState,
    onNicknameChanged: (String) -> Unit,
    onClickPrimaryAction: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val nicknameFeedback = uiState.nicknameValidation.toNicknameFeedback()
    val submitButtonText = stringResource(R.string.feature_profile_impl_submit_button_text)

    Column(modifier = modifier.fillMaxSize()) {
        ProfileScreenTopAppBar(
            isCreate = uiState is ProfileUiState.Create,
            onBack = onBack,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = PrezelTheme.spacing.V20)
                .padding(top = PrezelTheme.spacing.V16),
        ) {
            PrezelAvatar(
                imageUrl = null,
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(120.dp),
            )

            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

            PrezelTextField(
                value = uiState.nickname,
                onValueChange = onNicknameChanged,
                label = stringResource(R.string.feature_profile_impl_nickname_text_field_label),
                placeholder = stringResource(R.string.feature_profile_impl_nickname_text_field_placeholder),
                modifier = Modifier.fillMaxWidth(),
                feedback = nicknameFeedback,
            )

            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))
        }

        PrezelButtonArea(
            showBackground = true,
            modifier = Modifier.imePadding(),
        ) {
            MainButton(
                label = submitButtonText,
                enabled = uiState.isPrimaryActionEnabled,
                onClick = onClickPrimaryAction,
            )
        }
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
            onClickPrimaryAction = {},
            onBack = {},
        )
    }
}

@BasicPreview
@Composable
private fun EditProfileScreenPreview() {
    PrezelTheme {
        ProfileScreen(
            uiState = ProfileUiState.Edit(originalNickname = ""),
            onNicknameChanged = {},
            onClickPrimaryAction = {},
            onBack = {},
        )
    }
}
