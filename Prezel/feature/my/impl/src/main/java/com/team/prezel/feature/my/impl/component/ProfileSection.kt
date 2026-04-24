package com.team.prezel.feature.my.impl.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.team.prezel.core.designsystem.component.PrezelAvatar
import com.team.prezel.core.designsystem.component.PrezelAvatarSize
import com.team.prezel.core.designsystem.component.actions.button.PrezelButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme

@Composable
internal fun ProfileSection(
    nickname: String,
    profileImageUrl: String?,
    onClickEditProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PrezelAvatar(
            imageUrl = profileImageUrl,
            contentDescription = "프로필 이미지",
            size = PrezelAvatarSize.REGULAR,
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))

        Text(
            text = nickname,
            style = PrezelTheme.typography.body1Bold,
            color = PrezelTheme.colors.textLarge,
        )

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V16))

        PrezelButton(
            text = "프로필 편집",
            iconResId = PrezelIcons.Edit,
            type = ButtonType.OUTLINED,
            size = ButtonSize.XSMALL,
            hierarchy = ButtonHierarchy.SECONDARY,
            onClick = onClickEditProfile,
        )
    }
}

@BasicPreview
@Composable
private fun ProfileSectionPreview() {
    PrezelTheme {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            ProfileSection(
                nickname = "닉네임",
                profileImageUrl = null,
                onClickEditProfile = {},
            )
        }
    }
}
