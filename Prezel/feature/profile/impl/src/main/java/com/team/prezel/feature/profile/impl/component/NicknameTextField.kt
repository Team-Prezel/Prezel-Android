package com.team.prezel.feature.profile.impl.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.textfield.PrezelTextField
import com.team.prezel.core.designsystem.component.textfield.PrezelTextFieldFeedback
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.profile.impl.R
import com.team.prezel.feature.profile.impl.model.NicknameValidationState

@Composable
internal fun NicknameTextField(
    nickname: String,
    onNicknameChanged: (String) -> Unit,
    nicknameValidationState: NicknameValidationState,
    modifier: Modifier = Modifier,
) {
    PrezelTextField(
        value = nickname,
        onValueChange = onNicknameChanged,
        label = stringResource(R.string.feature_profile_impl_nickname_text_field_label),
        placeholder = stringResource(R.string.feature_profile_impl_nickname_text_field_placeholder),
        feedback = nicknameValidationState.toNicknameFeedback(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
        ),
        modifier = modifier.fillMaxWidth(),
    )
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
private fun NicknameTextFieldPreview() {
    PrezelTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            NicknameTextField(
                nickname = "",
                onNicknameChanged = {},
                nicknameValidationState = NicknameValidationState.Unchecked,
            )
        }
    }
}
