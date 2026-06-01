package com.team.prezel.feature.feedback.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class FeedbackNavKey(
    val presentationId: Long,
    val title: String,
    val isPast: Boolean = false,
) : NavKey
