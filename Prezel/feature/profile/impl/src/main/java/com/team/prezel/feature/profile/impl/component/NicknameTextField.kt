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
import com.team.prezel.core.designsystem.component.textfield.PrezelTextFieldStatus
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.model.profile.Nickname
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
        status = nicknameValidationState.toNicknameStatus(),
        maxLength = Nickname.MAX_LENGTH,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
        ),
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
private fun NicknameValidationState.toNicknameStatus(): PrezelTextFieldStatus =
    when (this) {
        NicknameValidationState.Unchecked,
        NicknameValidationState.Checking,
        NicknameValidationState.TooLong,
        -> PrezelTextFieldStatus.DEFAULT

        NicknameValidationState.Available -> PrezelTextFieldStatus.Default(
            message = stringResource(R.string.feature_profile_impl_nickname_helper_available),
        )

        NicknameValidationState.TooShort -> PrezelTextFieldStatus.Bad(
            message = stringResource(R.string.feature_profile_impl_nickname_helper_too_short),
        )

        NicknameValidationState.Duplicated -> PrezelTextFieldStatus.Bad(
            message = stringResource(R.string.feature_profile_impl_nickname_helper_duplicated),
        )

        NicknameValidationState.InvalidCharacter -> PrezelTextFieldStatus.Bad(
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
