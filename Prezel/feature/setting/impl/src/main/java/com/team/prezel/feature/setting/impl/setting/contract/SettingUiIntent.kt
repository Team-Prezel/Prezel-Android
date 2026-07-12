package com.team.prezel.feature.setting.impl.setting.contract

import com.team.prezel.core.ui.base.UiIntent

internal sealed interface SettingUiIntent : UiIntent {
    data object FetchData : SettingUiIntent

    data object ClickTermsOfService : SettingUiIntent

    data object ClickPrivacyPolicy : SettingUiIntent

    data object ClickLogout : SettingUiIntent
}
