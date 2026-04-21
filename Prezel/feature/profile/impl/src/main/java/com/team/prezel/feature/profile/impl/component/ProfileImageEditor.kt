package com.team.prezel.feature.profile.impl.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.PrezelAvatar
import com.team.prezel.core.designsystem.component.actions.button.PrezelIconButton
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonHierarchy
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonSize
import com.team.prezel.core.designsystem.component.actions.button.config.ButtonType
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.profile.impl.R

@Composable
internal fun ProfileImageEditor(
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

@BasicPreview
@Composable
private fun ProfileImageEditorPreview() {
    PrezelTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ProfileImageEditor(
                profileUrl = "",
                isDefaultProfileImage = true,
                onClick = {},
            )

            ProfileImageEditor(
                profileUrl = "https://picsum.photos/200",
                isDefaultProfileImage = false,
                onClick = {},
            )
        }
    }
}
