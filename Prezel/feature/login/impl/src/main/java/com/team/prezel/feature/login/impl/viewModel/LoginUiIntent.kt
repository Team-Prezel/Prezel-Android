package com.team.prezel.feature.login.impl.viewModel

import android.content.Context

sealed interface LoginUiIntent {
    data class OnClickLogin(
        val context: Context,
        val failureMessage: String,
        val rateLimitMessage: String,
    ) : LoginUiIntent
}
