package com.team.prezel.feature.setting.impl.setting.contract

import com.team.prezel.core.ui.base.UiEffect
import com.team.prezel.feature.setting.impl.setting.model.SettingUiMessage

internal sealed interface SettingUiEffect : UiEffect {
    data object NavigateToSplash : SettingUiEffect

    data class NavigateToTermsDetail(
        val title: String,
        val url: String,
    ) : SettingUiEffect

    data class ShowMessage(
        val message: SettingUiMessage,
    ) : SettingUiEffect
}
