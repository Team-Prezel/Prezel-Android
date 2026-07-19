package com.team.prezel.feature.profile.impl.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team.prezel.core.designsystem.component.PrezelTopAppBar
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.feature.profile.impl.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileScreenTopAppBar(
    isCreate: Boolean,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    PrezelTopAppBar(
        modifier = modifier,
        title = stringResource(
            id = if (isCreate) R.string.feature_profile_impl_topbar_create_title else R.string.feature_profile_impl_topbar_edit_title,
        ),
    ) {
        LeadingIcon(
            iconResId = PrezelIcons.ArrowLeft,
            contentDescription = stringResource(R.string.feature_profile_impl_topbar_leading_icon_content_description),
            onClick = onBack,
        )
    }
}

@BasicPreview
@Composable
private fun ProfileScreenTopAppBarCreatePreview() {
    PrezelTheme {
        ProfileScreenTopAppBar(
            isCreate = true,
            onBack = {},
        )
    }
}

@BasicPreview
@Composable
private fun ProfileScreenTopAppBarEditPreview() {
    PrezelTheme {
        ProfileScreenTopAppBar(
            isCreate = false,
            onBack = {},
        )
    }
}
