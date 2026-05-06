package com.team.prezel.feature.setting.impl.delete.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Bullet
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.team.prezel.core.designsystem.component.PrezelCheckbox
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.util.noRippleClickable
import com.team.prezel.feature.setting.impl.R

@Composable
internal fun DeleteAccountNoticeStep(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = PrezelTheme.spacing.V20)
            .padding(top = PrezelTheme.spacing.V16, bottom = PrezelTheme.spacing.V24),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V16),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8)) {
            Text(
                text = stringResource(R.string.feature_setting_impl_delete_account_notice_title),
                style = PrezelTheme.typography.title2Bold,
                color = PrezelTheme.colors.textLarge,
            )
            DeleteAccountNoticeDescription()
        }

        DeleteAccountNoticeDetailBox()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable { onCheckedChange(!isChecked) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PrezelCheckbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                extraTouchPadding = PaddingValues(end = PrezelTheme.spacing.V8),
            )
            Text(
                text = stringResource(R.string.feature_setting_impl_delete_account_notice_check),
                style = PrezelTheme.typography.body3Regular,
                color = PrezelTheme.colors.textRegular,
            )
        }
    }
}

@Composable
private fun DeleteAccountNoticeDescription(modifier: Modifier = Modifier) {
    val regularStyle = SpanStyle(
        color = PrezelTheme.colors.textMedium,
        fontSize = PrezelTheme.typography.body2Regular.fontSize,
        fontWeight = PrezelTheme.typography.body2Regular.fontWeight,
        letterSpacing = PrezelTheme.typography.body2Regular.letterSpacing,
    )
    val highlightStyle = SpanStyle(
        color = PrezelTheme.colors.feedbackBadRegular,
        fontSize = PrezelTheme.typography.body2Bold.fontSize,
        fontWeight = PrezelTheme.typography.body2Bold.fontWeight,
        letterSpacing = PrezelTheme.typography.body2Bold.letterSpacing,
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        Text(
            text = stringResource(R.string.feature_setting_impl_delete_account_notice_description_1),
            style = PrezelTheme.typography.body2Regular,
            color = PrezelTheme.colors.textMedium,
        )
        Text(
            text = buildAnnotatedString {
                withStyle(regularStyle) {
                    append(stringResource(R.string.feature_setting_impl_delete_account_notice_description_2_prefix))
                    append(" ")
                }
                withStyle(highlightStyle) {
                    append(stringResource(R.string.feature_setting_impl_delete_account_notice_description_2_highlight))
                }
                withStyle(regularStyle) {
                    append(stringResource(R.string.feature_setting_impl_delete_account_notice_description_2_suffix))
                }
            },
            style = PrezelTheme.typography.body2Regular,
        )
        Text(
            text = stringResource(R.string.feature_setting_impl_delete_account_notice_description_3),
            style = PrezelTheme.typography.body2Bold,
            color = PrezelTheme.colors.textMedium,
        )
    }
}

@Composable
private fun DeleteAccountNoticeDetailBox(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = PrezelTheme.colors.bgMedium,
                shape = PrezelTheme.shapes.V8,
            ).padding(horizontal = PrezelTheme.spacing.V8, vertical = PrezelTheme.spacing.V12),
        verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8),
    ) {
        DeleteAccountNoticeDetailItem(
            prefix = stringResource(R.string.feature_setting_impl_delete_account_notice_detail_1_prefix),
            highlight = stringResource(R.string.feature_setting_impl_delete_account_notice_detail_1_highlight),
            suffix = stringResource(R.string.feature_setting_impl_delete_account_notice_detail_1_suffix),
        )
        DeleteAccountNoticeDetailItem(
            prefix = stringResource(R.string.feature_setting_impl_delete_account_notice_detail_2_prefix),
            highlight = stringResource(R.string.feature_setting_impl_delete_account_notice_detail_2_highlight),
            suffix = stringResource(R.string.feature_setting_impl_delete_account_notice_detail_2_suffix),
        )
        DeleteAccountNoticeDetailItem(
            text = stringResource(R.string.feature_setting_impl_delete_account_notice_detail_3),
        )
        DeleteAccountNoticeDetailItem(
            prefix = stringResource(R.string.feature_setting_impl_delete_account_notice_detail_4_prefix),
            highlight = stringResource(R.string.feature_setting_impl_delete_account_notice_detail_4_highlight),
        )
    }
}

@Composable
private fun DeleteAccountNoticeDetailItem(
    modifier: Modifier = Modifier,
    text: String? = null,
    prefix: String = "",
    highlight: String = "",
    suffix: String = "",
) {
    val regularStyle = SpanStyle(
        color = PrezelTheme.colors.textMedium,
        fontSize = PrezelTheme.typography.body3Regular.fontSize,
        fontWeight = PrezelTheme.typography.body3Regular.fontWeight,
        letterSpacing = PrezelTheme.typography.body3Regular.letterSpacing,
    )
    val highlightStyle = SpanStyle(
        color = PrezelTheme.colors.feedbackBadRegular,
        fontSize = PrezelTheme.typography.body3Regular.fontSize,
        fontWeight = PrezelTheme.typography.body2Bold.fontWeight,
        letterSpacing = PrezelTheme.typography.body3Regular.letterSpacing,
    )
    val detailText: AnnotatedString = buildAnnotatedString {
        withBulletList(bullet = Bullet.Default.copy(padding = 0.5.em)) {
            withBulletListItem {
                if (text != null) {
                    append(text)
                } else {
                    withStyle(regularStyle) {
                        append(prefix)
                        if (prefix.isNotEmpty()) append(" ")
                    }
                    withStyle(highlightStyle) { append(highlight) }
                    withStyle(regularStyle) {
                        if (suffix.isNotEmpty()) append(" ")
                        append(suffix)
                    }
                }
            }
        }
    }

    Text(
        text = detailText,
        modifier = modifier.fillMaxWidth(),
        style = PrezelTheme.typography.body3Regular,
        color = PrezelTheme.colors.textMedium,
    )
}

@BasicPreview
@Composable
private fun DeleteAccountNoticeStepPreview() {
    PrezelTheme {
        DeleteAccountNoticeStep(
            isChecked = false,
            onCheckedChange = {},
        )
    }
}
