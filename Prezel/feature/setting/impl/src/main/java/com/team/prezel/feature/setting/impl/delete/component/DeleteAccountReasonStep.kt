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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun DeleteAccountReasonStep(
    selectedReason: DeleteAccountReasonOption?,
    otherReasonText: String,
    onSelectReason: (DeleteAccountReasonOption) -> Unit,
    onOtherReasonChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = PrezelTheme.spacing.V12)
            .padding(top = PrezelTheme.spacing.V16, bottom = PrezelTheme.spacing.V24),
    ) {
        DeleteAccountReasonHeader(modifier = Modifier.padding(horizontal = PrezelTheme.spacing.V8))

        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V32))

        DeleteAccountReasons(
            selectedReason = selectedReason,
            otherReasonText = otherReasonText,
            onSelectReason = onSelectReason,
            onOtherReasonChanged = onOtherReasonChanged,
        )
    }
}

@Composable
private fun DeleteAccountReasonHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.feature_setting_impl_delete_account_reason_title),
            style = PrezelTheme.typography.title2Bold,
            color = PrezelTheme.colors.textLarge,
        )
        Spacer(modifier = Modifier.height(PrezelTheme.spacing.V8))
        Text(
            text = stringResource(R.string.feature_setting_impl_delete_account_reason_description),
            style = PrezelTheme.typography.body3Regular,
            color = PrezelTheme.colors.textRegular,
        )
    }
}

@Composable
private fun DeleteAccountReasons(
    selectedReason: DeleteAccountReasonOption?,
    otherReasonText: String,
    onSelectReason: (DeleteAccountReasonOption) -> Unit,
    onOtherReasonChanged: (String) -> Unit,
    reasonOptions: ImmutableList<Pair<DeleteAccountReasonOption, String>> = persistentListOf(
        DeleteAccountReasonOption.Etc to stringResource(R.string.feature_setting_impl_delete_account_reason_other),
        DeleteAccountReasonOption.NotUsedOften to stringResource(R.string.feature_setting_impl_delete_account_reason_not_used_often),
        DeleteAccountReasonOption.NoLongerNeeded to stringResource(R.string.feature_setting_impl_delete_account_reason_no_longer_needed),
        DeleteAccountReasonOption.TooComplex to stringResource(R.string.feature_setting_impl_delete_account_reason_too_difficult_or_complex),
        DeleteAccountReasonOption.InaccurateAnalysis to stringResource(
            R.string.feature_setting_impl_delete_account_reason_analysis_result_inaccurate,
        ),
        DeleteAccountReasonOption.ManyErrors to stringResource(R.string.feature_setting_impl_delete_account_reason_too_many_errors),
    ),
) {
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
            modifier = Modifier.noRippleClickable { onSelectReason(reason) },
        )

        if (reason == DeleteAccountReasonOption.Etc && selectedReason == DeleteAccountReasonOption.Etc) {
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
            selectedReason = DeleteAccountReasonOption.Etc,
            otherReasonText = "",
            onSelectReason = {},
            onOtherReasonChanged = {},
        )
    }
}
