package com.team.prezel.feature.setting.impl.delete.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.team.prezel.core.designsystem.component.PrezelRadio
import com.team.prezel.core.designsystem.component.list.PrezelList
import com.team.prezel.core.designsystem.component.textfield.PrezelTextArea
import com.team.prezel.core.designsystem.preview.BasicPreview
import com.team.prezel.core.designsystem.theme.PrezelTheme
import com.team.prezel.core.ui.util.noRippleClickable
import com.team.prezel.feature.setting.impl.R
import com.team.prezel.feature.setting.impl.delete.model.DeleteAccountReasonOption

@Composable
internal fun DeleteAccountReasonStep(
    selectedReason: DeleteAccountReasonOption?,
    otherReasonText: String,
    onSelectReason: (DeleteAccountReasonOption) -> Unit,
    onOtherReasonChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val reasonOptions = listOf(
        DeleteAccountReasonOption.OTHER to stringResource(R.string.feature_setting_impl_delete_account_reason_other),
        DeleteAccountReasonOption.NOT_USED_OFTEN to stringResource(R.string.feature_setting_impl_delete_account_reason_not_used_often),
        DeleteAccountReasonOption.NO_LONGER_NEEDED to stringResource(R.string.feature_setting_impl_delete_account_reason_no_longer_needed),
        DeleteAccountReasonOption.TOO_DIFFICULT_OR_COMPLEX to
            stringResource(R.string.feature_setting_impl_delete_account_reason_too_difficult_or_complex),
        DeleteAccountReasonOption.ANALYSIS_RESULT_INACCURATE to
            stringResource(R.string.feature_setting_impl_delete_account_reason_analysis_result_inaccurate),
        DeleteAccountReasonOption.TOO_MANY_ERRORS to stringResource(R.string.feature_setting_impl_delete_account_reason_too_many_errors),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = PrezelTheme.spacing.V12)
            .padding(top = PrezelTheme.spacing.V16, bottom = PrezelTheme.spacing.V24),
    ) {
        DeleteAccountReasonHeader()

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        Column(verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V12)) {
            CompositionLocalProvider(LocalContentColor provides PrezelTheme.colors.textLarge) {
                reasonOptions.forEach { (reason, label) ->
                    DeleteAccountReasonOptionItem(
                        reason = reason,
                        label = label,
                        selectedReason = selectedReason,
                        otherReasonText = otherReasonText,
                        onSelectReason = onSelectReason,
                        onOtherReasonChanged = onOtherReasonChanged,
                    )
                }
            }
        }
    }
}

@Composable
private fun DeleteAccountReasonHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(PrezelTheme.spacing.V8)) {
        Text(
            text = stringResource(R.string.feature_setting_impl_delete_account_reason_title),
            style = PrezelTheme.typography.title2Bold,
            color = PrezelTheme.colors.textLarge,
        )
        Text(
            text = stringResource(R.string.feature_setting_impl_delete_account_reason_description),
            style = PrezelTheme.typography.body3Regular,
            color = PrezelTheme.colors.textRegular,
        )
    }
}

@Composable
private fun DeleteAccountReasonOptionItem(
    reason: DeleteAccountReasonOption,
    label: String,
    selectedReason: DeleteAccountReasonOption?,
    otherReasonText: String,
    onSelectReason: (DeleteAccountReasonOption) -> Unit,
    onOtherReasonChanged: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        PrezelList(
            title = label,
            titleTextColor = PrezelTheme.colors.textLarge,
            nested = true,
            leadingContent = {
                PrezelRadio(
                    checked = selectedReason == reason,
                    onCheckedChange = { checked -> if (checked) onSelectReason(reason) },
                )
            },
            modifier = Modifier.noRippleClickable(
                onClick = { onSelectReason(reason) },
            ),
        )

        if (reason == DeleteAccountReasonOption.OTHER && selectedReason == DeleteAccountReasonOption.OTHER) {
            Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))
            PrezelTextArea(
                value = otherReasonText,
                onValueChange = onOtherReasonChanged,
                placeholder = stringResource(R.string.feature_setting_impl_delete_account_reason_other_placeholder),
                maxLength = 200,
                showCount = false,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PrezelTheme.spacing.V8),
            )
        }
    }
}

@BasicPreview
@Composable
private fun DeleteAccountReasonStepPreview() {
    PrezelTheme {
        DeleteAccountReasonStep(
            selectedReason = DeleteAccountReasonOption.NOT_USED_OFTEN,
            otherReasonText = "",
            onSelectReason = {},
            onOtherReasonChanged = {},
        )
    }
}

@BasicPreview
@Composable
private fun DeleteAccountReasonStepOtherPreview() {
    PrezelTheme {
        DeleteAccountReasonStep(
            selectedReason = DeleteAccountReasonOption.OTHER,
            otherReasonText = "기타 사유를 입력합니다.",
            onSelectReason = {},
            onOtherReasonChanged = {},
        )
    }
}
