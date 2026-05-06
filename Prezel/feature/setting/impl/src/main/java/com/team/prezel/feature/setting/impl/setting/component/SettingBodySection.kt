package com.team.prezel.feature.setting.impl.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.team.prezel.core.designsystem.component.PrezelAvatar
import com.team.prezel.core.designsystem.component.PrezelAvatarSize
import com.team.prezel.core.designsystem.component.PrezelHorizontalDivider
import com.team.prezel.core.designsystem.component.list.PrezelList
import com.team.prezel.core.designsystem.icon.PrezelIcons
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.util.noRippleClickable
import com.team.prezel.feature.setting.impl.R
import com.team.prezel.feature.setting.impl.setting.contract.SettingUiState

@Composable
internal fun SettingBodySection(
    uiState: SettingUiState,
    onClickTermsOfService: () -> Unit,
    onClickPrivacyPolicy: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        AccountSection(
            profileImageUrl = uiState.profileImageUrl,
            nickname = uiState.nickname,
            email = uiState.email,
        )

        PrezelHorizontalDivider(color = PrezelTheme.colors.bgMedium)

        PolicySection(
            onClickTermsOfService = onClickTermsOfService,
            onClickPrivacyPolicy = onClickPrivacyPolicy,
        )
    }
}

@BasicPreview
@Composable
private fun SettingBodySectionPreview() {
    PrezelTheme {
        SettingBodySection(
            uiState = SettingUiState(
                nickname = "발표잘하고싶어요",
                email = "email@email.com",
            ),
            onClickTermsOfService = {},
            onClickPrivacyPolicy = {},
        )
    }
}

@Composable
internal fun AccountSection(
    profileImageUrl: String?,
    nickname: String,
    email: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = PrezelTheme.spacing.V20,
                vertical = PrezelTheme.spacing.V16,
            ),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    ) {
        PrezelAvatar(
            imageUrl = profileImageUrl,
            contentDescription = stringResource(R.string.feature_setting_impl_profile_image_content_description),
            size = PrezelAvatarSize.SMALL,
        )

        Spacer(modifier = Modifier.width(PrezelTheme.spacing.V12))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = nickname,
                color = PrezelTheme.colors.textLarge,
                style = PrezelTheme.typography.body2Medium,
            )

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text(
                    text = email,
                    color = PrezelTheme.colors.textRegular,
                    style = PrezelTheme.typography.body3Medium,
                )
                Spacer(modifier = Modifier.width(PrezelTheme.spacing.V4))
                Icon(
                    painter = painterResource(PrezelIcons.Kakao),
                    contentDescription = stringResource(R.string.feature_setting_impl_kakao_account_content_description),
                    tint = PrezelTheme.colors.solidBlack,
                    modifier = Modifier
                        .size(16.dp)
                        .clip(PrezelTheme.shapes.V1000)
                        .background(Color(0xFFFEE500))
                        .padding(3.dp),
                )
            }
        }
    }
}

@BasicPreview
@Composable
private fun AccountSectionPreview() {
    PrezelTheme {
        AccountSection(
            profileImageUrl = null,
            nickname = "발표잘하고싶어요",
            email = "email@email.com",
        )
    }
}

@Composable
internal fun PolicySection(
    onClickTermsOfService: () -> Unit,
    onClickPrivacyPolicy: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(
            horizontal = PrezelTheme.spacing.V20,
            vertical = PrezelTheme.spacing.V28,
        ),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V20),
    ) {
        CompositionLocalProvider(
            LocalContentColor provides PrezelTheme.colors.textLarge,
        ) {
            val trailingIcon = @Composable {
                Icon(
                    painter = painterResource(PrezelIcons.ChevronRight),
                    contentDescription = null,
                    tint = PrezelTheme.colors.iconRegular,
                )
            }

            PrezelList(
                title = stringResource(R.string.feature_setting_impl_terms_of_service),
                nested = true,
                trailingContent = trailingIcon,
                modifier = Modifier.noRippleClickable(
                    onClick = onClickTermsOfService,
                ),
            )
            PrezelList(
                title = stringResource(R.string.feature_setting_impl_privacy_policy),
                nested = true,
                trailingContent = trailingIcon,
                modifier = Modifier.noRippleClickable(
                    onClick = onClickPrivacyPolicy,
                ),
            )
        }
    }
}

@BasicPreview
@Composable
private fun PolicySectionPreview() {
    PrezelTheme {
        PolicySection(
            onClickTermsOfService = {},
            onClickPrivacyPolicy = {},
        )
    }
}
