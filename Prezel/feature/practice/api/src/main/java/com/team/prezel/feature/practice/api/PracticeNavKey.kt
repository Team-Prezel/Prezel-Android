package com.team.prezel.feature.practice.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface PracticeNavKey : NavKey {
    @Serializable
    data object Recording : PracticeNavKey

    @Serializable
    data class Analysis(
        val recordingFilePath: String,
        val referenceText: String,
    ) : PracticeNavKey
}
